package org.ailingo.app.features.topics.presentation

import ailingo.composeapp.generated.resources.Res
import ailingo.composeapp.generated.resources.business
import ailingo.composeapp.generated.resources.cartoon
import ailingo.composeapp.generated.resources.cartoons
import ailingo.composeapp.generated.resources.culture
import ailingo.composeapp.generated.resources.culture_and_art
import ailingo.composeapp.generated.resources.fashion
import ailingo.composeapp.generated.resources.fashion_and_style
import ailingo.composeapp.generated.resources.film
import ailingo.composeapp.generated.resources.food
import ailingo.composeapp.generated.resources.food_and_drinks
import ailingo.composeapp.generated.resources.health
import ailingo.composeapp.generated.resources.health_and_medicine
import ailingo.composeapp.generated.resources.history
import ailingo.composeapp.generated.resources.literature
import ailingo.composeapp.generated.resources.movies
import ailingo.composeapp.generated.resources.nature
import ailingo.composeapp.generated.resources.nature_and_ecology
import ailingo.composeapp.generated.resources.news
import ailingo.composeapp.generated.resources.science
import ailingo.composeapp.generated.resources.science_and_education
import ailingo.composeapp.generated.resources.sport
import ailingo.composeapp.generated.resources.technologies
import ailingo.composeapp.generated.resources.technology
import ailingo.composeapp.generated.resources.trips
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.ailingo.app.core.utils.windowinfo.info.WindowInfo
import org.ailingo.app.features.topics.data.Topic

@Composable
fun TopicsScreen(
    windowInfo: WindowInfo
) {
    val topics = listOf(
        Topic(Res.string.trips, Res.drawable.trips),
        Topic(Res.string.food_and_drinks, Res.drawable.food),
        Topic(Res.string.movies, Res.drawable.film),
        Topic(Res.string.cartoons, Res.drawable.cartoon),
        Topic(Res.string.culture_and_art, Res.drawable.culture),
        Topic(Res.string.technologies, Res.drawable.technology),
        Topic(Res.string.fashion_and_style, Res.drawable.fashion),
        Topic(Res.string.news, Res.drawable.news),
        Topic(Res.string.health_and_medicine, Res.drawable.health),
        Topic(Res.string.science_and_education, Res.drawable.science),
        Topic(Res.string.sport, Res.drawable.sport),
        Topic(Res.string.literature, Res.drawable.literature),
        Topic(Res.string.nature_and_ecology, Res.drawable.nature),
        Topic(Res.string.history, Res.drawable.history),
        Topic(Res.string.business, Res.drawable.business)
    )

    if (windowInfo.screenWidthInfo is WindowInfo.WindowType.DesktopWindowInfo) {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Adaptive(350.dp),
            verticalItemSpacing = 4.dp,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            contentPadding = PaddingValues(end = 16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(topics) { photo ->
                ContentTopics(photo, type = "desktop")
            }
        }
    } else {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Adaptive(150.dp),
            verticalItemSpacing = 4.dp,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            contentPadding = PaddingValues(4.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(topics) { photo ->
                ContentTopics(photo, type = "mobile")
            }
        }
    }
}
