# React Native File Upload

A module support upload file to server using https://github.com/loopj/android-async-http. Support add header, multipart-form

### Usage
```
var {FileUploader} = require('react-native-file-uploader');

let header = {
}
//Optional
FileUploader.setHeaders(headers)

let key_upload = "file_upload";
let params = {
  param1: ...,
  param2: ...
}
params[key_upload] = file_path

FileUploader.upload(url, params, key_upload,  function(error, data){
  if (!error) {
    console.log("Login data: ", data);
  } else {
    console.log("Error: ", error);
  }
})
```


### Install

- Run in your project:
```sh
$ npm i -S https://github.com/leonacky/react-native-file-uploader.git
```

#### iOS
Comming soon

#### Android

1. In `android/setting.gradle`

    ```
    ...
    include ':react-native-file-uploader'
    project(':react-native-file-uploader').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-file-uploader/android')
    ```

2. In `android/app/build.gradle`

    ```
    ...
    dependencies {
        ...
        compile project(':react-native-file-uploader')
    }
    ```

3. Register module (in MainApplication.java)

    ```
    import com.aotasoft.rngooglelogin.FileUploaderPackage;  // <--- import

    public class MainApplication extends Application implements ReactApplication {
      ......

      @Override
      protected List<ReactPackage> getPackages() {
        return Arrays.<ReactPackage>asList(
          new MainReactPackage(),
          new VectorIconsPackage(),
          new OrientationPackage(this),
          new FileUploaderPackage()   // <--- Add here!
      );
    }

      ......

    }
    ```

