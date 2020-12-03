
![image](https://user-images.githubusercontent.com/61506233/100989312-15140e80-3594-11eb-956e-30632ad1a4c9.png)
- 딥러닝 기반 자동편집 블랙박스 어플리케이션

## 1) 시연영상
- 유트브 링크 : 


## 2) 프로젝트 소개
### 개발 배경
도로의 CCTV 역할을 하는 블랙박스는 현대사회에서 각종 사고 및 범죄 발생시 필수적인 증거 자료 활용되고있다. 
하지만 촬영한 블랙박스 영상은 대용량 파일이기에 저장/보관/관리하는데에 어려움이 존재한다. 실제로, 블랙박스 영상에서 원하는 부분을 가져오려면 전체영상을 일일이 돌려봐야하는 시간적/자원적 비효율성이 존재했다. 기존의 블랙박스는 센서를 이용하여 본인 차량에 충격이 탐지되었을 때에만 따로 저장할 수 있기 때문에, 본인 차량의 충격 감지상황 이외에 발생할 수 있는 사고 및 범죄에 대한 영상증거물을 확보하기 어렵다. 

### 제안 내용
따라서 본 프로젝트에서는 컴퓨터비전, 음성인식 관점에서 비정상적인 상황을 탐지하고 이부분만 따로 저장할 수 있도록 하는 기능을 제공하여 블랙박스 영상관리를 효율적으로 할 수 있도록 돕는다.

자동편집 상황 예시)
- 주행중 : 교통사고
- 주차중 : 데이트폭력 및 추행, 납치, 기물파손, 배회, 폭행, 싸움, 절도, 실신, 침입, 투기, 강도, 납치, 주취행동 등 
- 비명소리, 굉음


### 기능 및 시나리오
#### 1) 녹화
![image](https://user-images.githubusercontent.com/61506233/101011507-f4ed4b00-35a5-11eb-9a4d-36f57e398a98.png)

- 스마트폰 카메라를 이용하여 주행 영상을 촬영
- 시간, 주소 정보를 화면에 표시 
#### 2) 동영상 편집
![image](https://user-images.githubusercontent.com/61506233/101012520-4dbce380-35a6-11eb-9d7c-702b56e7beeb.png)
- 저장된 영상을 불러와서 편집해주는 서비스 제공
- 주행 영상, 주차 영상을 구분해서 업로드하게 한다.
   - 2-1) 충격 탐지 시 저장
     - 본인 차량의 충격 탐지 시 영상 저장
   - 2-2) abnormal event detection 탐지 시 저장
     - 비정상적인 상황에 대한 영상 저장

### 기술설명
#### 1. 충격탐지 
- 기존기술 사용(안드로이드 API)
#### 2. Abnormal Event 탐지
- 딥러닝 사용(Vision, Audio)

### System Overview
![image](https://user-images.githubusercontent.com/61506233/100989472-41c82600-3594-11eb-9ce9-3675edf1dbdb.png)
![image](https://user-images.githubusercontent.com/61506233/100991239-1e05df80-3596-11eb-9a5c-5473357e3f8b.png)

## 3) Reference
- [1] Yao, Y., Xu, M., Wang, Y., Crandall, D.J., Atkins, "Unsupervised traffic accident detection in first-person videos", in IROS, 2019  
- [2] W. Sultani, C. Chen, and M. Shah, "Real-World Anomaly Detection in Surveillance Videos", in CVPR, 2018
- [3] K. He, G. Gkioxari, P. Dollar, and R. Girshick, “Mask R-CNN,” in ICCV, 2017 
- [4] N. Wojke, A. Bewley, and D. Paulus, “Simple online and realtime tracking with a deep association metric,” in ICIP, 2017 
- [5] E. Ilg, N. Mayer, T. Saikia, M. Keuper, A. Dosovitskiy, and T. Brox, “Flownet 2.0: Evolution of optical flow estimation with deep networks,” in CVPR, 2017. 
- [6] R. Mur-Artal and J. D. Tardos, “Orb-slam2: An open-source slam ´ system for monocular, stereo, and rgb-d cameras,” T-RO, 2017. 

## 4) 기술 블로그
- 딥러닝 기술블로그 : https://kkminyoung.tistory.com/14
- 안드로이드 기술블로그1 : https://csepotato.tistory.com/
- 안드로이드 기술블로그2 : https://jungyoonn.tistory.com/
- 백엔드 기술블로그 : https://cococode.tistory.com/

## 5) License


# 프로젝트 레파지토리 정리
- 딥러닝 repository : https://github.com/kkminyoung/Capstone_model
- 안드로이드 repository : https://github.com/kkminyoung/Capstone_android
- 서버 repository : https://github.com/Kim-Ha-Jeong/Capstone_server

