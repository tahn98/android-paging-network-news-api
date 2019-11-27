# ReadNews
Demo application uses Paging in Android Jetpack framework.

### PageKeyedDataSource 
DataSource is queried to load pages of content into a <b>PagedList</b>. A PagedList can grow as it loads more data, but the data loaded cannot be updated. If the underlying data set is modified, a new PagedList / DataSource pair must be created to represent the new data.

Use PageKeyedDataSource if pages you load embed keys for loading adjacent pages. For example a network response that returns some items, and a next/previous page links.

<i>Snippet code load init</i>
```kotlin
private fun createLoadInit(
        params : LoadInitialParams<Int>,
        callback : LoadInitialCallback<Int, Article>,
        currentPage : Int,
        nextPage : Int
    ){
        api.getArticlesSync(
            country = country,
            page = currentPage,
            perPage = params.requestedLoadSize,
            onPrepared = {
                postInitialState(NetworkState.LOADING)
            },
            onSuccess = {response ->
                val items = response?.data ?: emptyList()
                retry = null
                postInitialState(NetworkState.LOADED)
                callback.onResult(items, null, nextPage)
            },
            onError = {errorMessage ->
                retry ={loadInitial(params, callback)}
                postInitialState(NetworkState.error(errorMessage))
            })
    }
```

### DataSourceFactory
You can then load this customized data into PagedList objects by creating a concrete subclass of DataSource.Factory.

### Repository 
Config pagedList and return Listing class
```kotlin
override fun getTopHeadlines(country: String, pageSize: Int): Listing<Article> {
        val factory = DataSourceFactory(api, country, networkExecutor)

        val config = pagedListConfig(pageSize)

        val livePagedList = LivePagedListBuilder(factory, config)
            .setFetchExecutor(networkExecutor)
            .build()

        return Listing(
            pagedList = livePagedList,
            networkState = switchMap(factory.sourceLiveData) { it.networkState },
            retry = {factory.sourceLiveData.value?.retryAllFailed()},
            refresh = { factory.sourceLiveData.value?.invalidate() },
            refreshState = switchMap(factory.sourceLiveData) { it.initialLoad })
    }
 ```
 
 ### ViewModel
 Hold data and fire message to UI
 ```kotlin
    private val country = MutableLiveData<String>()
    private val articleResult = map(country) {
        repository.getTopHeadlines(it, PAGE_SIZE)
    }
    val listArticles = switchMap(articleResult) { it.pagedList }
    val networkState = switchMap(articleResult) { it.networkState }
    val refreshState = switchMap(articleResult) { it.refreshState }
    
    fun showResults(country: String): Boolean {
        if (this.country.value == country) {
            return false
        }
        this.country.value = country
        return true
    }
```