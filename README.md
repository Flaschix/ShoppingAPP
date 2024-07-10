- [1. ~ Architecture](#1--architecture-)
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

## 1. ~ Architecure :

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

## 3. ~ Data Layer :

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

Вот кст вам скриншоты экрана:

![image](https://github.com/Flaschix/ShoppingAPP/assets/89143685/2babf791-d0c3-4720-bded-71dba6d2f29b) ![image](https://github.com/Flaschix/ShoppingAPP/assets/89143685/7b355823-8d9b-4169-8f92-d85df59c4938)


### 4.2. SignIn Screen:

Экран авторизации мы будем делать по темже принципам, так что я просто покажу вам конечный результат как это выглядит (совсем забыл, так же я добавил возможность сброса пароля для пользователя):

```kotlin
  viewModelScope.launch {
      resetPasswordUseCase(email).collect{
          when(it){
              is ResultNet.Error -> _resetPassword.emit(ResetPasswordState.Error(it.message))
              ResultNet.Initial -> {}
              ResultNet.Loading -> _resetPassword.emit(ResetPasswordState.Loading)
              is ResultNet.Success<*> -> _resetPassword.emit(ResetPasswordState.Success(email))
          }
      }
  }
```

![image](https://github.com/Flaschix/ShoppingAPP/assets/89143685/b097478b-612c-40f1-b0f8-16a84edeeac0) ![image](https://github.com/Flaschix/ShoppingAPP/assets/89143685/7ee4d42c-68a3-412b-88a7-162139765397)


### 4.3. Main Screen:

На главной странице у нас была настроенна панель категорий и перемещения по фрагментам при помощи неё

```kotlin
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoriesFragment = arrayListOf<Fragment>(
            MainCategoryFragment(),
            MaskFragment(),
            SuntanFragment(),
            FaceFragment(),
            BodyFragment(),
            InRoadFragment()
        )

        binding.viewpagerHome.isUserInputEnabled = false

        val viewPagerAdapter = HomeAdapter(categoriesFragment, childFragmentManager, lifecycle)

        binding.viewpagerHome.adapter = viewPagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewpagerHome) { tab, pos ->
            when(pos){
                0 -> tab.text = "Главная"
                1 -> tab.text = "Тело"
                2 -> tab.text = "Загар"
                3 -> tab.text = "Лицо"
                4 -> tab.text = "Маски"
                5 -> tab.text = "В дорогу"
            }
        }.attach()

    }
```

![image](https://github.com/Flaschix/ShoppingAPP/assets/89143685/76a5b2f5-3975-4405-88d5-dddd0052d740)

Для экрана **Главная** был создан отдельный дизайн и настроен свой собственный адаптер вот основная часть кода


```kotlin
class MainCategoryFragment: Fragment(R.layout.fragment_main_category) {

    private var _binding: FragmentMainCategoryBinding? = null

    private val binding: FragmentMainCategoryBinding
        get() = _binding ?: throw Exception("FragmentMainCategoryBinding === null")

    private val specialProductAdapter: SpecialProductAdapter by lazy {
        SpecialProductAdapter()
    }

    private val bestCaseAdapter: BestCaseAdapter by lazy {
        BestCaseAdapter()
    }

    private val bestProductAdapter: BestProductAdapter by lazy {
        BestProductAdapter()
    }

    private val viewModel by viewModels<MainCategoryViewModel>()

    override fun onCreateView(
        ...
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpSPRV()
        setUpBCRV()
        setUpBPRV()

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.screenState.collect{
                    when(it){
                        is RVState.Error -> {}
                        RVState.Initial -> {}
                        RVState.Loading -> {
                            showProgress()
                        }
                        is RVState.Success -> {
                            specialProductAdapter.submitList(it.data)
                            bestCaseAdapter.submitList(it.data)
                            bestProductAdapter.submitList(it.data)
                            hideProgress()
                        }
                    }
                }
            }
        }
    }
    private fun setUpSPRV(){
        binding.rvSpecialProducts.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = specialProductAdapter
        }

        specialProductAdapter.onProductClickListener = {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToShopItemFragment(it))
        }

        bestCaseAdapter.onProductClickListener = {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToShopItemFragment(it))
        }

        bestProductAdapter.onProductClickListener = {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToShopItemFragment(it))
        }
    }

    private fun setUpBCRV(){
        binding.rvBestDealsProducts.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = bestCaseAdapter
        }
    }

    private fun setUpBPRV(){
        binding.rvBestProducts.apply {
            layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            adapter = bestProductAdapter
        }
    }

    private fun showProgress() {
        binding.mainCategoryProgressbar.visibility = View.VISIBLE
        binding.bestProductsProgressbar.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        binding.mainCategoryProgressbar.visibility = View.GONE
        binding.bestProductsProgressbar.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        showBottomNavigationView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
```

Также был создан специальный **sealed** класс разных категорий экранов, чтобы было удобнее орентироваться и точно не ошибиться в написании

```kotlin
sealed class Category(val category: String) {

    object Mask: Category("mask")
    object Face: Category("face")
    object Suntan: Category("suntan")
    object Body: Category("body")
    object InRoad: Category("inroad")
}
```


Для остальных категорий был создан один шаблон который просто заполнялся пришедшими в него данными:

![image](https://github.com/Flaschix/ShoppingAPP/assets/89143685/65824a90-682a-48ae-96c6-55830cd3c861)

Их viewmodel выглядит довольно компактно:

```kotlin
class BaseCategoryViewModel(
    private val getListProductByCategoryUseCase: GetListProductByCategoryUseCase,
    private val category: Category
): ViewModel(){
    private val productList = getListProductByCategoryUseCase(category.category)



    val screenState = productList
        .filter { it.isNotEmpty() }
        .map { RVState.Success(it) as RVState }
        .onStart { emit(RVState.Loading) }
}
```

### 4.4. Basket Screen:

Далее перейдём к экрану отвечающему за корзину. Она имеет два основных вида: пустая и с чем-то. На данном экране можно увидеть все товары что были добавлены пользователем в корзину, их общую стоймость, а также имеется функция удаления товара из корзины.

![image](https://github.com/Flaschix/ShoppingAPP/assets/89143685/e5d5c47f-3f65-4192-af67-45cbdf7bf37f) ![image](https://github.com/Flaschix/ShoppingAPP/assets/89143685/4a6fd557-5298-419a-a858-76601537fdfd)


Также как и всегда во viewmodel мы просто запрашиваем данные и передаём их нашему view. **View** в свою очередь просто подписывается на их рассылку и настраивает списки.

```kotlin
@HiltViewModel
class BasketViewModel @Inject constructor(
    private val getBasketUseCase: GetBasketUseCase,
    private val deleteProductFromBasketUseCase: DeleteProductFromBasketUseCase
): ViewModel() {

    private val basket = getBasketUseCase()

    val basketState = basket
        .catch {  }
        .map { BasketState.Success(it) as BasketState }
        .onStart { BasketState.Loading }

    val productsPrice = basketState.map {
        when(it){
            is BasketState.Success -> sumProductPrice(it.data)
            else -> null
        }
    }

    private fun sumProductPrice(products: List<Product>): Int {
        return products.sumOf { it.price.priceWithDiscount.toInt() }
    }

    fun deleteProductFromBasket(product: Product) {

        viewModelScope.launch {
            deleteProductFromBasketUseCase(product).collect{

            }
        }

    }
}
```

Кстати про настраивание списков. Покажу вам на примере корзины как я это делаю.

Наш класс наследуется от ListAdapter и принимает в себя тип данных которые он будет принимать, viewholder который будет создавать и как параметр принимает класс кастомный DiffUtil который ьудет говорить ему изменились ли данные. Так же в него добавлены слушатели для внешней настройки нажатия на кнопки и элементы.

```kotlin
class BasketAdapter: ListAdapter<Product, BasketAdapter.BasketViewHolder>(
    ProductDiffCallback()
){
    var deleteClickListener: ((Product) -> Unit)? = null

    var onProductClickListener: ((Product) -> Unit)? = null

    inner class BasketViewHolder(val binding: BasketProductItemBinding): ViewHolder(binding.root){
        fun bind(product: Product){
            binding.apply {
                Glide.with(itemView).load(product.images[0]).into(imageCartProduct)
                tvProductCartName.text = product.title
                tvProductCartPrice.text = "${product.price.priceWithDiscount} ₽"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasketViewHolder {
        return BasketViewHolder(
            BasketProductItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: BasketViewHolder, position: Int) {
        val product = getItem(position)

        holder.bind(product)

        holder.binding.imageMinus.setOnClickListener{
            deleteClickListener?.invoke(product)
        }

        holder.itemView.setOnClickListener {
            onProductClickListener?.invoke(product)
        }
    }
}
```

Вот как выглядит класс наш класс **ProductDiffCallback**

```kotlin
class ProductDiffCallback : DiffUtil.ItemCallback<Product>(){
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }
}
```

### 4.5. Product Screen:

Далее расмотрим экран с полной информацией о продукте. В него можно попасть по нажатию на продукт из мест где их видно, такие как напиремер гланый экран, корзина и тд. 

На данном экране мы можем увидеть всю информацию о товаре, опместить его в корзину, расмотреть все его изображения и тд. Подробно показывать код не буду, тк все всё там идёт по тому же принципу что и до этого было.

![image](https://github.com/Flaschix/ShoppingAPP/assets/89143685/5545f682-33dc-40de-a889-d071a5f98b0d) ![image](https://github.com/Flaschix/ShoppingAPP/assets/89143685/f6b754ee-95ec-4f64-926b-618211472eb3)

### 4.6. Profile Screen:

Последним экраном на данный момент является экран профиля пользователя. Тут все дефолтно. Можно посмотреть данные о пользователи и выйти из аккаунта. В дальнейшем будет доработка.

![image](https://github.com/Flaschix/ShoppingAPP/assets/89143685/32de848e-e512-49fb-9da6-3bfffd4413db)



## 5. ~ DI :

### 5.1. AppModule:

Совсем немного затрону внедрение зависимостей **DI**. До этого я работал только с библиотекой Dagger 2 и в ней столько всего есть что настроить и разобраться порой довольно долго. Так что в этом проекте решил пропробовать что-то новое и менее большое, тк для моего проекта думал что хватит.

Здесь я буду использовать Hilt - более лёгкую версию Dagger 2.

Для начала нужно создать точку входа нашего приложения

```kotlin
@HiltAndroidApp
class AndroidAppH: Application() {
}
```

Далее мы можем использовать все наши любимые анотации Inject во всех местах где машина сама сможет сгенерировать нам данные.

В более сложных случаях мы должны сами объяснить откуда что взять или как что создать нашему другу. Для этого я создал **AppModule** и добавил в него все нужные мне реализации. Также добавил к ним анатации singleton чтобы они не создовались каждый раз заново.

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore() = Firebase.firestore

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return ApiFactory.apiService
    }
}
```

### 5.2. RepositoryModule:

Для реализации интерфейсов был создал дополнительный модуль.

```kotlin
@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    @Singleton
    fun provideMainRepositoryImpl(repository: ProductRepositoryImpl): ProductRepository

    @Binds
    @Singleton
    fun provideUserRepositoryImpl(repository: UserRepositoryImpl): UserRepository
}
```


**Проект пока не доделан. В дальнейшем буду продолжать его развивать и дополнять функционалом.**

