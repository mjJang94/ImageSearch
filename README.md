
### 네이버 검색 API를 활용한 Android 샘플 앱

평소에 관심 있거나 직접 구현해보고 싶던 **Architecture, DI, Network**를 적용하여 구현해 보았습니다.

구현된 코드를 계속해서 보완하며 적용하고 싶은 것들을 추가할 예정입니다.:smile:

### 이미지 검색 API 가이드  
https://developers.naver.com/docs/serviceapi/search/image/image.md#%EC%9D%B4%EB%AF%B8%EC%A7%80

### 개발 환경   

<a href="https://https://developer.android.com/studio/intro?hl=ko"><img src="https://img.shields.io/badge/Android Studio-3DDC84?style=flat-square&logo=Android Studio&logoColor=white"/></a>
<a href="https://kotlinlang.org/docs/releases.html#release-details"><img src="https://img.shields.io/badge/Kotlin 1.6.21-7F52FF?style=flat-square&logo=Kotlin&logoColor=white"/></a>
<img src="https://img.shields.io/badge/Git-F05032?style=flat-square&logo=Git&logoColor=white"/>


### Architecture
MVVM + 구글 권장 아키텍처

<img src="/architecture.png" width="600px" height="350px"></img><br/>

### 구성 Library
- Paging3
- LifeCycle
- DataBinding
- Retrofit2
- Room
- Glide
- Dagger-hilt
- Coroutines
- Timber

### Review

1. 구글에서 권장하는 안드로이드 아키텍처를 기반으로 관심사 분리를 위해 구조를 **UI Layer, Domain Layer, Data Layer**로 분리하였습니다.

- **UI Layer** - 유저 상호작용, 외부 입력 값(네트워크, DB)에 의해 데이터가 변할때마다 변경사항에 대한 UI 업데이트를 위해 데이터 홀더 클래스인 ViewModel을 사용하였습니다. 각 Activity, Fragment에서 필요로 하는 ViewModel이 관리하는 데이터 혹은 이벤트를 Observe하여 변화에 따라 UI 요소들을 업데이트 하도록 구성하였습니다. 또한 Lifecycle에 따라 불필요한 observe를 하지 않도록 고민하였습니다.

- **Domain Layer** - 복잡한 비즈니스 로직, 혹은 여러 ViewModel에서 재사용될 수 있는 로직을 담당하기 위해 추가하였습니다. 초기 버전인 1.0.0에서는 효율 보다는 비용이 더 많이 들었지만 추가 개발에 의해 ViewModel이 늘어나거나 동일한 로직이 필요할때 유용하게 사용할 수 있을 것 같아 추가하게 되었습니다. 해당 레이어에서는 각각 하나의 기능을 구현한 UseCase 클래스로 구성되어 있고, ViewModel에서 필요한 기능에 따라 알맞는 UseCase를 주입받아 사용하도록 구현했습니다.

- **Data Layer** - 앱의 데이터 생성, 저장, 삭제를 담당하고, 이제 따라 local, remote 데이터를 처리하기 위한 데이터 소스 클래스로 구성되어 있습니다. 하나의 데이터 소스로 여러가지 데이터 처리를 할 수 있도록 구현하였습니다.

2. 홈 버튼을 눌러서 화면이 Background에 진입하게 된 경우, 변경된 UIEvnet 데이터에 의해 화면을 업데이트 해줄 필요가 없으므로 UI가 사용자에게 보여지고 있지 않을 땐 데이터를 observe 하지 않도록 Lifecycle.repeatOnLifecycle()을 사용하였습니다. 해당 메소드를 사용하게 된 이유는 지정한 Lifecycle 상태에 맞춰 알아서 코루틴 블록을 실행할지 취소할지 스스로 판단하기 때문에, 부가적인 코드를 줄일 수 있었습니다.   
   
   [[코드]](https://github.com/mjJang94/ImageSearch/blob/master/app/src/main/java/com/mj/imagesearch/ui/main/ImageSearchFragment.kt)


### 테스트 코드



