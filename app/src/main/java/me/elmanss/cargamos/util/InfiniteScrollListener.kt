package me.elmanss.cargamos.util

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


/**
 * CGS on 27/02/20.
 *
 * Esta clase está basada en el el siguiente tutorial:
 * @see <a href="https://www.nickmillward.com/android/2018/3/1/infinite-scrolling-with-recyclerview-and-unsplash-api">
 *    Infinite Scrolling // Endlessly scroll with RecyclerView and Unsplash API by Nick Millward
 *     </a>
 *
 */
abstract class InfiniteScrollListener(private val layoutManager: GridLayoutManager) :
    RecyclerView.OnScrollListener() {
    private var minItemsBeforeNextLoad = 5
    private val startingPage = 1
    private var currentPage = 1
    private var latestTotalItemCount = 0
    private var isLoading = true

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
        val totalItemCount = layoutManager.itemCount

        // Assume list was invalidated -- set back to default
        if (totalItemCount < latestTotalItemCount) {
            currentPage = startingPage
            latestTotalItemCount = totalItemCount
        }
        // If still loading and dataset size has been updated, update load state and last item count
        if (isLoading && totalItemCount > latestTotalItemCount) {
            isLoading = false
            latestTotalItemCount = totalItemCount
        }
        // If not loading and within threashold limit, increase current page and load more data
        if (!isLoading && lastVisibleItemPosition + minItemsBeforeNextLoad > totalItemCount) {
            currentPage++
            onLoadMore(currentPage, totalItemCount, recyclerView)
            isLoading = true
        }
    }

    abstract fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?)

    init {
        minItemsBeforeNextLoad *= layoutManager.spanCount
    }
}