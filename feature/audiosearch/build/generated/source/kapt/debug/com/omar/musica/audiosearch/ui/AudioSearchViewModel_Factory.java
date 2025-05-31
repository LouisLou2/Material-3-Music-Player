package com.omar.musica.audiosearch.ui;

import android.content.Context;
import com.omar.musica.audiosearch.data.recorder.AudioRecorder;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class AudioSearchViewModel_Factory implements Factory<AudioSearchViewModel> {
  private final Provider<Context> contextProvider;

  private final Provider<AudioRecorder> audioRecorderProvider;

  public AudioSearchViewModel_Factory(Provider<Context> contextProvider,
      Provider<AudioRecorder> audioRecorderProvider) {
    this.contextProvider = contextProvider;
    this.audioRecorderProvider = audioRecorderProvider;
  }

  @Override
  public AudioSearchViewModel get() {
    return newInstance(contextProvider.get(), audioRecorderProvider.get());
  }

  public static AudioSearchViewModel_Factory create(Provider<Context> contextProvider,
      Provider<AudioRecorder> audioRecorderProvider) {
    return new AudioSearchViewModel_Factory(contextProvider, audioRecorderProvider);
  }

  public static AudioSearchViewModel newInstance(Context context, AudioRecorder audioRecorder) {
    return new AudioSearchViewModel(context, audioRecorder);
  }
}
