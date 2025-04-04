package com.example.data.mapper

import com.example.data.local.model.NewsItemDbModel
import com.example.data.network.model.news.NewsResponseDto
import com.example.data.network.model.weather.WeatherResponseDto
import com.example.domain.news.model.NewsItem
import com.example.domain.weather.model.Weather

internal fun WeatherResponseDto.mapResponseToDomainWeather(): Weather {
    val temperature = this.main.temp
    val humidity = this.main.humidity
    val windSpeed = this.wind.speed
    val main = this.weather.first().main
    val description = this.weather.first().description

    return Weather(
        temperature = temperature.toInt(),
        main = main,
        description = description,
        windSpeed = windSpeed,
        humidity = humidity
    )
}

internal fun NewsItemDbModel.mapToDomainNewsItem(): NewsItem {
    val newsItem = NewsItem(
        id = this.id,
        source = this.source,
        author = this.author,
        title = this.title,
        description = this.description,
        url = this.url,
        urlToImage = this.urlToImage,
        publishedAt = this.publishedAt,
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
