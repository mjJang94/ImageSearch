package com.mj.imagesearch.resource

import com.mj.data.local.FavoriteImageEntity
import com.mj.data.remote.ImageSearchResponse
import com.mj.data.remote.model.ItemResponse
import com.mj.domain.model.ThumbnailData

val itemResponseMock = listOf(
    ItemResponse(
        title = "Pin page 2003 Blue Bird Vision School Bus With Front Side Power Handicap Door. | School bus, Bus, School",
        link = "https://i.pinimg.com/736x/ae/b4/6a/aeb46a89cf34e756d57a77641f4bc505--school-buses-blue-bird.jpg",
        thumbnail = "https://search.pstatic.net/sunny/?src=https://i.pinimg.com/736x/ae/b4/6a/aeb46a89cf34e756d57a77641f4bc505--school-buses-blue-bird.jpg&type=b150",
        sizeHeight = 494,
        sizeWidth = 736
    )
)

val getImageMock = ImageSearchResponse(
    lastBuildDate = "Fri, 19 Aug 2022 15:16:15 +0900",
    total = 407783,
    start = 1,
    display = 10,
    items = itemResponseMock
)

val getFavoriteMock = listOf(
    ThumbnailData(
        uid = 0,
        thumbnail = "https://search.pstatic.net/sunny/?src=https://i.pinimg.com/736x/ae/b4/6a/aeb46a89cf34e756d57a77641f4bc505--school-buses-blue-bird.jpg&type=b150",
    )
)

