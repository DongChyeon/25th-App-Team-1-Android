package com.yapp.setting.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.designsystem.theme.OrbitTheme

@Composable
fun UserInfoCard() {
    Box(
        modifier = Modifier
            .background(
                color = OrbitTheme.colors.gray_700,
                shape = RoundedCornerShape(20.dp),
            )
            .fillMaxWidth()
            .padding(24.dp),
    ) {
        Column(
            modifier = Modifier.wrapContentWidth(),
        ) {
            UserNameAndGenderText(name = "강문수", gender = "남성")
            Spacer(modifier = Modifier.height(4.dp))
            UserBirthDayText(birth = "양력 1999년 7월 8일")
        }
        Icon(
            painter = painterResource(id = core.designsystem.R.drawable.ic_arrow_right),
            contentDescription = "",
            modifier = Modifier.align(Alignment.CenterEnd),
            tint = OrbitTheme.colors.gray_300,
        )
    }
}

@Composable
fun UserNameAndGenderText(name: String, gender: String) {
    Row(
        modifier = Modifier.wrapContentWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = name,
            style = OrbitTheme.typography.headline1SemiBold,
            color = OrbitTheme.colors.white,
        )
        Spacer(modifier = Modifier.width(4.dp))
        Icon(
            painter = painterResource(id = core.designsystem.R.drawable.ic_circle),
            contentDescription = "컴마",
            modifier = Modifier.size(3.dp),
            tint = OrbitTheme.colors.gray_300,
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = gender,
            style = OrbitTheme.typography.headline1SemiBold,
            color = OrbitTheme.colors.white,
        )
    }
}

@Composable
fun UserBirthDayText(birth: String) {
    Text(
        modifier = Modifier.wrapContentWidth(),
        text = birth,
        style = OrbitTheme.typography.body1Regular,
        color = OrbitTheme.colors.gray_50,
    )
}

@Composable
@Preview
fun UserInfoCardPreview() {
    UserInfoCard()
}
