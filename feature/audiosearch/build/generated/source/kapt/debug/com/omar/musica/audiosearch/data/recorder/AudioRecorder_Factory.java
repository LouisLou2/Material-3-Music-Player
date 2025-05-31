package com.omar.musica.audiosearch.data.recorder;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
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
public final class AudioRecorder_Factory implements Factory<AudioRecorder> {
  @Override
  public AudioRecorder get() {
    return newInstance();
  }

  public static AudioRecorder_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static AudioRecorder newInstance() {
    return new AudioRecorder();
  }

  private static final class InstanceHolder {
    private static final AudioRecorder_Factory INSTANCE = new AudioRecorder_Factory();
  }
}
