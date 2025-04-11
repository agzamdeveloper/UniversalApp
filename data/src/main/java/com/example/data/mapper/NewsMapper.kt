package com.example.data.mapper

import android.annotation.SuppressLint
import com.example.data.local.model.NewsItemDbModel
import com.example.data.network.model.news.NewsResponseDto
import com.example.domain.news.model.NewsItem
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

internal fun NewsItemDbModel.mapToDomainNewsItem(): NewsItem {
    val newsItem = NewsItem(
        id = this.id,
        source = this.source,
        author = this.author,
        title = this.title,
        description = this.description,
        url = this.url,
        urlToImage = this.urlToImage,
        publishedAt = formatDate(this.publishedAt),
        content = this.content,
        category = this.category,
        favourite = this.favourite
    )
    return newsItem
}

internal fun NewsResponseDto.mapToListNewsItemDbModel(category: String): List<NewsItemDbModel> {
    val listOfNewItemDbModel = mutableListOf<NewsItemDbModel>()
    for (newsDto in this.articles) {
        val newsItemDbModel = NewsItemDbModel(
            title = newsDto.title ?: "",
            source = newsDto.source?.name ?: "",
            author = newsDto.author ?: "",
            description = newsDto.description ?: "",
            url = newsDto.url ?: "",
            urlToImage = newsDto.urlToImage ?: "",
            publishedAt = newsDto.publishedAt ?: "",
            content = newsDto.content ?: "",
            category = category
        )

        listOfNewItemDbModel.add(newsItemDbModel)
    }

    return listOfNewItemDbModel
}

@SuppressLint("NewApi")
private fun formatDate(isoDate: String): String {
    val zonedDateTime = ZonedDateTime.parse(isoDate)
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.getDefault())
    return zonedDateTime.format(formatter)
}