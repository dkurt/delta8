<!-- .slide: class="center" -->
# Intel Delta8 practice

<!----------------------------------------------------------------------------->

---
## Day 1
### Agenda
* Divide onto teams
* Intel Galileo sample
* What will we make?

<!----------------------------------------------------------------------------->

---
<!-- .slide: class="center" -->
### Intel Galileo sample: Sobel edge detector

<!----------------------------------------------------------------------------->

---?code=src/sobel.ino&lang=cpp&title="Arduino sketch"

@[1-6]
@[8-10]

<!----------------------------------------------------------------------------->

---?code=src/sobel_board.py&lang=python&title="Device (server)"

@[1-7]
@[9-11]
@[13-17]
@[19-24]
@[26-31]
@[33-36]
@[39-47]

```bash
$ scp src/sobel_board.py root@x.x.x.x:/home/root
$ ssh root@x.x.x.x
root@x.x.x.x:~$ python sobel_board.py
```

@[-]

<!----------------------------------------------------------------------------->

---?code=src/sobel.py&lang=python&title="Host (client)"

@[1-10]
@[12-12]
@[14-33]
@[36-36]
@[38-41]
@[42-46]
@[48-50]
@[52-56]

```bash
$ python src/sobel.py -i x.x.x.x
```

@[-]

<!----------------------------------------------------------------------------->

---?image=images/canny.png&size=auto 90%

<!----------------------------------------------------------------------------->

---
### Available deep learning models
* Face detection
* Emotions recognition
* Face recognition
* Face generation
* Style transfer

<!----------------------------------------------------------------------------->

---?image=images/pipeline.png&size=auto 90%

<!----------------------------------------------------------------------------->

---
### My Delta
* **x<sub>0</sub>** - We're here
* **x<sub>0</sub> + δ** - Standalone application
* **x<sub>0</sub> + Δ** - Teamwork

---

## Day 2

---

### Face detector

|               |                                                              |
|---------------|--------------------------------------------------------------|
| Input         | any size BGR image, values in range `[0, 255]`               |
| Preprocessing | subtract mean:<br> `B - 104, G - 177, R - 123`               |
| Output        | Blob of bounding boxes with size `1x1xNx7`<br> where `N` - number of detections<br> and an every BBox is a vector<br>`id, classId, confidence, left, top, right, bottom` |

---

### Fast-neural-style

|               |                                                              |
|---------------|--------------------------------------------------------------|
| Input         | any size BGR image, values in range `[0, 255]`               |
| Preprocessing | subtract mean:<br>`B - 103.939, G - 116.779, R - 123.68`     |
| Output        | Stylized planar BGR image                                    |
| Postproc.     | add mean:<br>`B + 103.939, G + 116.779, R + 123.68`<br>clip to `[0, 255]` |

---

### BEGAN, faces generator

|               |                                                              |
|---------------|--------------------------------------------------------------|
| Input         | `1x64` uniform `[-1, 1]` noise                               |
| Output        | Planar RGB image, values in range `[-1, 1]`                  |


---

### Emotion recognition

|               |                                                              |
|---------------|--------------------------------------------------------------|
| Input         | `48x48` grayscale face image, values in range `[0, 1]`       |
| Output        | `1x7` vector of emotions "confidences": angry, disgusted, fearful, happy, sad, surprised, neutral |

---

### Face recognition

|               |                                                              |
|---------------|--------------------------------------------------------------|
| Input         | `96x96` RGB image normalized to `[0, 1]`                     |
| Output        | `1x128` embedding vector                                     |


<!----------------------------------------------------------------------------->

---

### Android + OpenCV "Hello, World!"

* Download and unpack OpenCV for Android:

  * Releases:
  <a href="https://github.com/opencv/opencv/releases"
     style="font-size: 90%">
      https://github.com/opencv/opencv/releases
  </a>
  * Nightly:
  <a href="http://pullrequest.opencv.org/buildbot/builders/master_pack-android"
     style="font-size: 90%">
      http://pullrequest.opencv.org/buildbot/builders/master_pack-android
  </a>

* Create a new empty project

<!----------------------------------------------------------------------------->

---?image=images/import_module.png&size=auto 90%

<!----------------------------------------------------------------------------->

---
### Update build configurations

* Open two files:
  ```
  project/app/build.gradle
  project/openCVLibrary331/build.gradle
  ```

* Copy values of `compileSdkVersion` and `buildToolsVersion` from the first
file to the second one:

  ~~compileSdkVersion 14~~ compileSdkVersion 26

  ~~buildToolsVersion "25.0.0"~~ buildToolsVersion "26.0.1"

* Make the project (no errors are expected).

<!----------------------------------------------------------------------------->

---?image=images/opencv_dependency_1.png&size=auto 90%

<!----------------------------------------------------------------------------->

---?image=images/opencv_dependency_2.png&size=auto 90%

<!----------------------------------------------------------------------------->

---
### Install OpenCV manager onto the device
Check CPU architecture
```bash
$ adb shell cat /proc/cpuinfo
```
```bash
> C:\Users\[user]\AppData\Local\Android\sdk\platform-tools\adb.exe ^
      shell cat /proc/cpuinfo
```
```bash
...
Processor: ARMv7 Processor rev 3 (v7l)
...
```

Install OpenCV manager for specific CPU:
```bash
adb install OpenCV-android-sdk/apk/OpenCV_3.3.1_Manager_3.31_armeabi-v7a.apk
```

<!----------------------------------------------------------------------------->

---?code=src/activity_main.xml&lang=xml&title="Modify activity_main.xml"

<!----------------------------------------------------------------------------->

---?code=src/AndroidManifest.xml&lang=xml&title="Modify AndroidManifest.xml"

@[1-3]
@[5-19]
@[21-26]
@[27-28]
@[-]

<!----------------------------------------------------------------------------->

---?code=src/MainActivity_show.java&lang=java&title="Write an application"

@[1-13]
@[15-15]
@[17-20]
@[22-27]
@[29-45]
@[47-65]

<!----------------------------------------------------------------------------->

---?code=src/MainActivity_show_send.java&lang=java&title="Get a feedback from device: server side"

@[24-25]
@[28-39]
@[41-58]
@[60-70]

<!----------------------------------------------------------------------------->

---?code=src/android_listener.py&lang=python&title="Get a feedback from device: host side"

@[1-7]
@[9-19]
@[21-25]

```bash
$ adb forward tcp:43656 tcp:43656
```
```bash
> C:\Users\[user]\AppData\Local\Android\sdk\platform-tools\adb.exe ^
    forward tcp:43656 tcp:43656
```

@[-]

<!----------------------------------------------------------------------------->
