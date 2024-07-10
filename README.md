- [1. ~ Architecture](#2--architecture-)
- [2. ~ Domain Layer](#2--domain-layer-)
  - [2.1. Entity:](#21-entity)
  - [2.2. Repository:](#22-repository)
  - [2.3. UseCase:](#23-usecase)
- [3. ~ Data Layer](#3--data-layer-)
  - [3.1. NetWork:](#31-network)
  - [3.2. Mapper:](#32-mapper)
  - [3.3. Repository Implementation:](#33-repository-implementation)
- [4. ~ Presentation Layer](#4--data-layer-)
  - [4.1. SignUp screen:](#41-signup-screen)
  - [4.2. SignIn screen:](#42-signin-screen)
  - [4.3. Main screen:](#43-main-screen)
  - [4.4. Basket screen:](#44-basket-screen)
  - [4.5. Product screen:](#45-product-screen)
  - [4.6. Profile screen:](#46-product-screen)
- [5. ~ DI](#5--data-layer-)
  - [5.1. AppModule:](#51-appmodule)
  - [5.2. RepositoryModule:](#52-signin)

Цель этого проекта — потренироваться в разработке мобильного приложения онлайн покупок по типу ozon и wildberries. Я стремлюсь создать интуитивно понятный интерфейс и обеспечить высокую производительность приложения, используя современные технологии и лучшие практики разработки.

## 1. ~ Architecure:

  Проект разработан с использованием принципов Clean Architecture и MVVM (Model-View-ViewModel). Это позволяет разделить логику приложения на независимые слои, что упрощает тестирование и поддержку кода.

**Для разработки приложения использовались следующие технологии и инструменты:**

1) Android SDK: Основной набор инструментов для разработки Android-приложений.

2) ViewPager2: Компонент для создания перелистываемых экранов.

3) Jetpack Navigation: Библиотека для управления навигацией в приложении.

4) Flow: Библиотека для работы с асинхронными потоками данных.

5) Hilt: Библиотека для внедрения зависимостей (DI).

6) Firebase: Будет использоваться для авторизации/регистрации пользователей и хранения их данных.

7) Retrofi+OkHttp: Библиотеки для работы с сетью.

8) Kotlin: Основной язык программирования для разработки приложения.

## 2. ~ Domain Layer :

Начнём мы разработку этого слоя с выявления основных сущностей и функционала приложения.

### 2.1. Entity:

1. **Класс User**

Основный класс который будет прдставлять наших пользователей.

```kotlin
data class User(
    val name: String,
    val surname: String,
    val mail: String,
    val password: String
){
    constructor(): this("","","","")
}
```

2. **Класс Product**

Данный класс будет представлять товары выставленные в нашем приложении. Он имеет в себе из небольшие кастомные подкласы, для упрощения понимания и лучшей читабельности.

```kotlin
@Parcelize
data class Product(
    val id: String,
    val title: String,
    val subtitle: String,
    val category: String,
    val images: List<String>,
    val price: ProductPrice,
    val feedback: ProductFeedback,
    val tags: List<String>,
    val available: Int,
    val description: String,
    val info: List<ProductInfo>,
    val ingredients: String,
) : Parcelable{
    constructor(): this("", "","","", emptyList(),ProductPrice(), ProductFeedback(), emptyList(), 0, "", emptyList(), "")
}
```

```kotlin
@Parcelize
data class ProductFeedback(
    val count: Int,
    val rating: Float
) : Parcelable{
    constructor(): this(0,0f)
}
```

```kotlin
@Parcelize
data class ProductInfo(
    val title: String,
    val value: String
) : Parcelable{
    constructor(): this("","")
}
```

```kotlin
@Parcelize
data class ProductPrice(
    val price: String,
    val discount: Int,
    val priceWithDiscount: String,
    val unit: String,
) : Parcelable{
    constructor(): this("", 0, "","")
}
```

Для класса User, Product и тд были созданы дефолтные конструкторы, чтобы в дальнейшем использовать авто-мапинг из firebase сущностей в наши.

3. **Изолированные Классы ResultNet**

Для более удобной работы с сетью и передачей данных между разными слоями, был создан специальный класс для отслеживания состояний.

```kotlin
sealed class ResultNet {
    data class Success<T>(val data: T) : ResultNet()
    data class Error(val message: String) : ResultNet()
    object Loading : ResultNet()

    object Initial : ResultNet()
}
```

### 2.2. Repository:

После создания основных сущностей проекта были созданы интерфейсы его репозиториев: **ProductRepository** и **UserRepository**. Они были логически разделены по функционалу. Один больше работает с пользователем и его данными. Другой с общей базо данных продуктов.

```kotlin
interface ProductRepository {

    fun getListProduct(): StateFlow<List<Product>>

    fun getListProductByCategory(category: String): StateFlow<List<Product>>
}
```

```kotlin
interface UserRepository {

    fun signUp(user: User): StateFlow<ResultNet>

    fun getUser(): StateFlow<User>

    fun signIn(email: String, password: String): StateFlow<ResultNet>

    fun signOut()

    fun resetPassword(email: String): StateFlow<ResultNet>

    fun getFavouriteList(phone: String): StateFlow<List<String>>

    fun getBasket(): StateFlow<List<Product>>

    fun addProductToBasket(product: Product): StateFlow<ResultNet>

    fun deleteProductFromBasket(product: Product): StateFlow<ResultNet>

    fun checkAuthState(): StateFlow<Boolean>
}
```

### 2.3. Repository:

И в завершении данного слоя были созданы отдельные use-case методы, в дальнейшем через них мы и будем получать и обрабатывать данные нашего приложения. Покажу только один из них.

```kotlin
сlass GetProductListUseCase @Inject constructor(private val repository: ProductRepository) {
    operator fun invoke(): StateFlow<List<Product>> {
        return repository.getListProduct()
    }
}
```

## 3. ~ Data Layer:

Далее был разработан слой работы с данными и сетью. В нём мы будем получать данные из сети, отправлять разные действия с сетью и базо данных, а так же обрабатывать их.

### 3.1. NetWork:

Начнём с получения доступа к основной базе данных наших товаров. Она для простоты будет сделанна просто в json фале и замокана в сеть.

В интрефейсе **ApiService** мы создаём наши методы для запросов в сеть. Указываем тип запроса, параметры если нужны путь к ним и возвращаемое значение.

```kotlin
interface ApiService {

    @GET("v3/0d2883f3-4db6-4374-bdd2-aa365c7cf466")
    suspend fun loadListProduct(): ListProductDto
}
```

Для правильного каста данных из сети в наши классы были созданы специальные модели, которые при помощи анатоции @SerializedName преобразуют данные из сети в поля.

Приведу пример двух основных классов:

```kotlin
data class ListProductDto(
    @SerializedName("items") val items: List<ProductDto>
)
```

```kotlin
data class ProductDto(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("subtitle") val subtitle: String,
    @SerializedName("category") val category: String,
    @SerializedName("images") val images: List<String>,
    @SerializedName("price") val price: PriceDto,
    @SerializedName("feedback") val feedback: FeedbackDto,
    @SerializedName("tags") val tags: List<String>,
    @SerializedName("available") val available: Int,
    @SerializedName("description") val description: String,
    @SerializedName("info") val info: List<InfoDto>,
    @SerializedName("ingredients") val ingredients: String
)
```

Далее нам требуется api которое будет предоставляет доступ к сети. Мы делаем его object чтобы данный объект был single-тоном и не создовался каждый раз при обращении из разных мест программы.

```kotlin
object ApiFactory {

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://run.mocky.io/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    val apiService: ApiService = retrofit.create()
}
```

### 3.2. Mapper:

Для преоброзования данных полученных из сети был создан специальнй класс **ProductMapper**. В нём храняться методы для преоброзования запросов из сети в объекты нашего приложения

```
class ProductMapper @Inject constructor() {
    fun mapResponseToProduct(response: ListProductDto): List<Product>{
        val result = mutableListOf<Product>()

        for (item in response.items){
            result.add(
                Product(
                    id = item.id,
                    title = item.title,
                    subtitle = item.subtitle,
                    ...
                )
            )
        }

        return result
    }

    ......
}
```

### 3.2. Repository Implementation:

В завершении нашего data слоя были созданы реализации интерфейсов репозиториев из нашего domain слоя. Они и будут нашими основными курьерами в сети и из неё:

Для примера покажу класс **ProductRepositoryImpl**. Он принимает в себя **mapper** для удобного преоброзования данных из сети, а также точку доступа в сеть в лице **apiService**.
В нём все просто. Мы запрашиваем данные, получаем их и эмитим всем кто подписался на их получение.

```kotlin
class ProductRepositoryImpl @Inject constructor(
    private val mapper: ProductMapper,
    private val apiService: ApiService
): ProductRepository {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val recommendations: StateFlow<List<Product>> = flow<List<Product>> {
        val response = apiService.loadListProduct()

        val products = mapper.mapResponseToProduct(response)
        emit(products.toList())
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Lazily,
        initialValue = listOf()
    )

    override fun getListProduct(): StateFlow<List<Product>> = recommendations

    override fun getListProductByCategory(category: String): StateFlow<List<Product>> = flow<List<Product>> {
        val response = apiService.loadListProduct()
        val products = mapper.mapResponseToProductByCategory(response, category)
        emit(products.toList())
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Lazily,
        initialValue = listOf()
    )

}
```

## 4. ~ Presentation Layer:

Мы написали бизнес слоя приложения, то что оно может делать и кто в нём живёт. Так же мы написали слой откуда и куда мы будем отправлять/получать наши данные, так же где будем их хранить и меня в течении использования нашего приложения.
Осталось только написать теперь слой который будет видеть пользователь и взаимодействовать со всем этим добром.

### 4.1. SignUp Screen:

Начнём с экрана регистрации (ведь нам нужны для начала пользователи, хоть какие-то...)

Этот экран будет давайть возможность зарегестрироваться, а также проверять валидность введёных данных. А для отслеживания успеха всего этого дела и взаимодействия с данными мы создадим **SignUpViewModel**, чтобы наша **view** не заморачивалась. Также это улучшит читабельность кода и сохранность наших данных.

В нашей модельке есть основные состояния, одно отвечает за успешность регистрации, а другое за валидность введёных данных.

```kotlin
    private val _signUpState = MutableStateFlow<SignUpState>(SignUpState.Initial)
    val signUpState: Flow<SignUpState> = _signUpState.asStateFlow()

    private val _validation = Channel<SignUpFieldsState>()
    val validateState = _validation.receiveAsFlow()
```

Для каждого поля идёт проверка коректности данных. Вот пример с полем **имени**.

```kotlin
    private fun validateName(name: String): SignUpValidateState{
        if(name.isEmpty()) return SignUpValidateState.Error("Fill this field")

        return SignUpValidateState.Success
    }
```

А вот основная функция которая и определит правильно ли пользователь ввёл свои данные

```kotlin
    private fun validateFields(user: User): Boolean{
        val nameV = validateName(user.name)
        val surnameV = validateName(user.surname)
        val emailV = validateEmail(user.mail)
        val passwordV = validatePassword(user.password)

        return emailV is SignUpValidateState.Success && passwordV is SignUpValidateState.Success &&
                nameV is SignUpValidateState.Success && surnameV is SignUpValidateState.Success
    }
```

А уже в функции регистрации если уж пользователь всё правильно ввёл, то мы говорим об этом нашей **view**

```kotlin
fun signUpUser(user: User){
        if (validateFields(user)){
            viewModelScope.launch {
                signUpUseCase(user).collect{
                    when(it){
                        is ResultNet.Error -> _signUpState.value = SignUpState.Error(it.message)
                        ResultNet.Initial -> {}
                        ResultNet.Loading -> _signUpState.emit(SignUpState.Loading)
                        is ResultNet.Success<*> -> _signUpState.value = SignUpState.Success
                    }
                }
            }
        }
    ..............
}
```


На самой же view мы просто подписываем на наши состояния. Вообще все view будут содержать минимум логики для поддержания clean принципа, так что в них мы просто настраиваем основные элементы и подписываемя на всё что нужно.

```kotlin
  viewLifecycleOwner.lifecycleScope.launch {
      repeatOnLifecycle(Lifecycle.State.STARTED){
          viewModel.signUpState.collect{
              when(it){
                  is SignUpState.Initial -> binding.prbSignUp.visibility = View.GONE
                  is SignUpState.Loading -> {
                      binding.btnSignUp.visibility = View.GONE
                      binding.prbSignUp.visibility = View.VISIBLE
                  }
                  is SignUpState.Success -> {
                      launchSignInFragment()
                  }
                  is SignUpState.Error -> {
                      Log.d("TEST", "onViewCreated: ${it.error}")
                      binding.btnSignUp.visibility = View.VISIBLE
                      binding.prbSignUp.visibility = View.GONE
                  }
              }
          }
      }
  }
```
