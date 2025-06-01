package com.omar.musica.tageditor.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omar.musica.store.TagsRepository
import com.omar.musica.store.model.tags.SongTags
import com.omar.musica.tageditor.state.TagEditorState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import java.io.FileNotFoundException


@HiltViewModel
class TagEditorViewModel @Inject constructor(
    private val tagsRepository: TagsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val songUri = Uri.parse(Uri.decode(savedStateHandle.get<String>("uri").orEmpty()))

    private val _state = MutableStateFlow<TagEditorState>(TagEditorState.Loading)
    val state: StateFlow<TagEditorState> get() = _state.asStateFlow()

    init {
        viewModelScope.launch {
            loadTags()
        }
    }

    private suspend fun loadTags() {
        val songTags = tagsRepository.getSongTags(songUri)
        _state.value = TagEditorState.Loaded(songTags)
    }

    fun saveTags(songTags: SongTags) {
        viewModelScope.launch {
            val currentState = state.value as TagEditorState.Loaded
            _state.value = currentState.copy(isSaving = true, isSaved = false, errorMessage = null)
            try {
                Timber.d("开始保存标签: ${songUri}")
                tagsRepository.editTags(songUri, songTags)
                Timber.d("标签保存成功")
                _state.getAndUpdate {
                    if (it is TagEditorState.Loaded)
                        it.copy(isSaved = true, isSaving = false, isFailed = false, errorMessage = null)
                    else
                        TagEditorState.Loading
                }
            } catch (e: Exception) {
                Timber.e(e, "标签保存失败")
                
                val errorMessage = when {
                    e.message?.contains("URI无效") == true -> 
                        "歌曲URI格式无效，无法定位文件。"
                    e.message?.contains("媒体库中未找到") == true -> 
                        "在媒体库中找不到该歌曲记录，可能歌曲已被删除。"
                    e.message?.contains("文件路径为空") == true -> 
                        "歌曲记录损坏，无法获取文件路径。"
                    e.message?.contains("Field null is not of type") == true -> 
                        "标签字段格式错误。某些标签信息可能包含无效字符。"
                    e.message?.contains("AbstractID3v2Frame") == true || e.message?.contains("AggregatedFrame") == true -> 
                        "ID3标签格式不兼容。建议清空有问题的字段后重试。"
                    e::class.java.simpleName.contains("CannotReadException") -> 
                        "无法读取音频文件。文件可能已损坏或格式不受支持。"
                    e::class.java.simpleName.contains("CannotWriteException") -> 
                        "无法写入音频文件。请检查文件权限。"
                    e is SecurityException -> 
                        "权限被拒绝。请授予存储访问权限。"
                    e is FileNotFoundException -> 
                        when {
                            e.message?.contains("音频文件不存在") == true -> "音频文件不存在，可能已被移动或删除。"
                            else -> "找不到音频文件。"
                        }
                    e is IllegalArgumentException -> 
                        when {
                            e.message?.contains("URI无效") == true -> "歌曲URI格式无效。"
                            else -> "不支持的音频文件格式或文件已损坏。"
                        }
                    else -> 
                        "保存失败: ${e.message ?: "未知错误"}"
                }
                
                _state.getAndUpdate {
                    if (it is TagEditorState.Loaded)
                        it.copy(isSaved = false, isSaving = false, isFailed = true, errorMessage = errorMessage)
                    else
                        TagEditorState.Loading
                }
            }
        }
    }

}