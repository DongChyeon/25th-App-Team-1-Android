package com.yapp.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yapp.designsystem.theme.OrbitTheme
import com.yapp.setting.component.InquiryCard
import com.yapp.setting.component.SettingItem
import com.yapp.setting.component.SettingTopAppBar
import com.yapp.setting.component.TableOfContentsText
import com.yapp.setting.component.UserInfoCard
import com.yapp.setting.component.VersionCodeText

@Composable
fun SettingRoute(
    viewModel: SettingViewModel = hiltViewModel(),
) {
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()

    SettingScreen(
        state = state,
        onNavigateToEditProfile = {
            viewModel.onAction(
                SettingContract.Action.NavigateToEditProfile,
            )
        },
        onBackClick = { viewModel.onAction(SettingContract.Action.PreviousStep) },
    )
}

@Composable
fun SettingScreen(
    state: SettingContract.State,
    onNavigateToEditProfile: () -> Unit,
    onBackClick: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OrbitTheme.colors.gray_900)
            .imePadding()
            .navigationBarsPadding(),
    ) {
        SettingTopAppBar(
            onBackClick = onBackClick,
            showTopAppBarActions = true,
            title = "설정",
        )
        Spacer(modifier = Modifier.height(12.dp))
        UserInfoCard(
            name = state.name,
            gender = state.selectedGender ?: "",
            birth = state.birthDate,
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .clickable { onNavigateToEditProfile() },
        )
        Spacer(modifier = Modifier.height(24.dp))
        InquiryCard(
            modifier = Modifier.padding(horizontal = 24.dp),
        )
        HorizontalDivider(
            modifier = Modifier.padding(top = 16.dp),
            thickness = 8.dp,
            color = OrbitTheme.colors.gray_800,
        )
        Spacer(modifier = Modifier.height(24.dp))
        TableOfContentsText(
            contentsTitle = "서비스 약관",
            modifier = Modifier.padding(horizontal = 24.dp),
        )
        Spacer(modifier = Modifier.height(12.dp))
        SettingItem(
            itemTitle = "이용약관",
            modifier = Modifier.padding(horizontal = 24.dp),
        )
        Spacer(modifier = Modifier.height(24.dp))
        SettingItem(
            itemTitle = "개인정보 처리방침",
            modifier = Modifier.padding(horizontal = 24.dp),
        )
        Spacer(modifier = Modifier.weight(1f))
        VersionCodeText(versionCode = "v1.0.0")
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
@Preview
fun SettingScreenPreview() {
    SettingScreen(
        state = SettingContract.State(),
        onNavigateToEditProfile = {},
    )
}
