
## 네이버 검색 API를 활용한 Android 샘플 앱

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

----

## Project Review

1. 구글에서 권장하는 안드로이드 아키텍처를 기반으로 관심사 분리를 위해 구조를 **UI Layer, Domain Layer, Data Layer**로 분리하였습니다.

- **UI Layer** - 유저 상호작용, 외부 입력 값(네트워크, DB)에 의해 데이터가 변할때마다 변경사항에 대한 UI 업데이트를 위해 데이터 홀더 클래스인 ViewModel을 사용하였습니다. 각 Activity, Fragment에서 필요로 하는 ViewModel이 관리하는 데이터 혹은 이벤트를 Observe하여 변화에 따라 UI 요소들을 업데이트 하도록 구성하였습니다. 또한 Lifecycle에 따라 불필요한 observe를 하지 않도록 고민하였습니다.

- **Domain Layer** - 복잡한 비즈니스 로직, 혹은 여러 ViewModel에서 재사용될 수 있는 로직을 담당하기 위해 추가하였습니다. 초기 버전인 1.0.0에서는 효율 보다는 비용이 더 많이 들었지만 추가 개발에 의해 ViewModel이 늘어나거나 동일한 로직이 필요할때 유용하게 사용할 수 있을 것 같아 추가하게 되었습니다. 해당 레이어에서는 각각 하나의 기능을 구현한 UseCase 클래스로 구성되어 있고, ViewModel에서 필요한 기능에 따라 알맞는 UseCase를 주입받아 사용하도록 구현했습니다.

- **Data Layer** - 앱의 데이터 생성, 저장, 삭제를 담당하고, 이제 따라 local, remote 데이터를 처리하기 위한 데이터 소스 클래스로 구성되어 있습니다. 하나의 데이터 소스로 여러가지 데이터 처리를 할 수 있도록 구현하였습니다.

2. 원칙적으로 ViewModel은 Presentation layer에 위치하고 있으므로 안드로이드 플랫폼과 최대한 관계가 없도록 구현하려고 시도했습니다.~~(하지만 Paging 라이브러리 내부 클래스참조가 불가피..)~~    
그러기 위해서 LiveData로 처리하던 모든 이벤트를 Sealed class(Event) + SharedFlow 조합으로 변경하여 Event 타입에 따라 Activity/Fragment에서 처리하도록 분기하였습니다.    
또한 홈 버튼을 눌러서 화면이 Background에 진입하게 된 경우, 변경된 UIEvent 데이터에 의해 화면을 업데이트 해줄 필요가 없으므로 UI가 사용자에게 보여지고 있지 않을 땐 데이터를 observe 하지 않도록 Lifecycle.repeatOnLifecycle()을 사용하였습니다.    
해당 메소드를 사용하게 된 이유는 지정한 Lifecycle 상태에 맞춰 알아서 코루틴 블록을 실행할지 취소할지 스스로 판단하기 때문에, 부가적인 코드를 줄일 수 있었습니다.

3. 리사이클러뷰 무한 스크롤 기능을 향상하기 위해 몇 가지 설정을 추가했습니다.

- **LayoutManager.setItemAnimator(null)** - RecyclerView의 깜빡임 현상의 원인이 될 수 있고 저사양 기기에서 버벅거림을 생기게 하는 기능이 될 수 있으므로 animation을 제거하였습니다.

- **LayoutManager.setItemPrefetchEnabled(true)** - UI Thread 에서 View 의 inflate & bind 작업이 완료되면 순차적으로 GPU Render Thread 에서 렌더링 작업을 하는데, 이때 UI Thread 는 유휴 상태가 됩니다.    
리사이클러뷰의 스크롤이 일어날 때 새로운 View 를 노출하기 위해 위 순서(inflate & bind)를 반복하게 되는데 문제는 위 작업의 비용이 크다 보니 Render Thread 에서 작업이 끝나고 사용자에게 노출되는 순간과 겹칠때가 있으므로 inflate & bind 작업과 동시에 렌더링 된 UI 가 표시됨으로 순간적인 버벅거림이 발생하는 케이스가 있을 수 있습니다.     
때문에 prefetch 방식을 이용하여 스크롤 할때 inflate 가 필요한 경우 미리미리 bind 해놓아야 하므로 setItemPrefetchEnabled() 는 기본값이 true 입니다.~~(굳이 명시해서 쓸 필욘 없지만 스터디가 목적이라서 명시적으로 작성했습니다.)~~     
하지만 스크롤이 조금만 발생하도록 의도된 UX 라던가, 전체 아이템의 개수가 한정적이거나 매우 적을 경우엔 prefetch 방식이 오히려 비용이 들어가는 문제가 될 수 있으므로 false 로 하는 것이 좋고, 반대로 스크롤이 많을 경우엔 true 가 유리합니다.

- **Glide.skipMemoryCache(false)** - 메모리가 극한으로 한정적인 상태인 것과 같이 특이한 케이스가 아닐 땐 캐싱을 위해 false로 지정하여 캐싱를 통해 빠른 이미지 호출 및 불필요한 네트워크 호출을 줄입니다.

- **Glide.format(DecodeFormat.PREFER_RGB_565)** - 썸네일 수준의 이미지 이므로 기본 포맷인 ARGB_8888을 RGB_565로 변경하여 색상 품질을 낮춰 메모리 효율을 올립니다. (약 50% 상승 기대)

- **Glide ImageView 고정 크기** - Glide를 통해 Image를 적용하려는 ImageView의 크기가 고정이 아닐 때 사용중인 기기 기준으로 이미지 해상도가 결정되어 의도치 않은 메모리 소비로 이어지고, GC의 호출이 증가함에 따라 성능 저하를 일으킬 수 있습니다. 이를 방지하기 위해 고정크기를 사용하였습니다.

### 테스트 코드

**Unit Case**

- HandleSearchImageSourceUseCase - MockData를 통해 테스트 Repository를 만들어 데이터를 정상적으로 응답 받았을 경우와 실패했을 경우로 나누어 테스트 코드를 작성했습니다.    
[[코드]](https://github.com/mjJang94/ImageSearch/blob/master/app/src/test/java/com/mj/imagesearch/usecase/HandleSearchImageSourceUseCase/CommonSearchViewModelTest.kt)
- HandleFavoriteImageSourceUseCase - MockData를 통해 테스트 Repository를 만들어 로컬 DB에 데이터를 insert, delete를 한 경우와 read 한 경우로 나누어 테스트 코드를 작성했습니다.     
[[코드]](https://github.com/mjJang94/ImageSearch/blob/master/app/src/test/java/com/mj/imagesearch/usecase/HandleFavoriteImageSourceUseCase/CommonFavoriteRepositoryTest.kt)

### 참조 블로그
- [[안드로이드 앱 아키텍처 가이드]](https://developer.android.com/jetpack/guide?hl=ko)
- [[RecyclerView 성능 향상]](https://gift123.tistory.com/67)


