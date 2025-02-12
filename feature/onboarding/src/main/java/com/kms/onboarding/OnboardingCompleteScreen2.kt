package com.kms.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kms.onboarding.component.OnBoardingTopAppBar
import com.yapp.designsystem.theme.OrbitTheme
import com.yapp.ui.component.button.OrbitButton
import com.yapp.ui.component.lottie.LottieAnimation
import com.yapp.ui.utils.heightForScreenPercentage
import com.yapp.ui.utils.paddingForScreenPercentage
import feature.onboarding.R

@Composable
fun OnboardingCompleteRoute2(
    viewModel: OnboardingViewModel,
    onFinishOnboarding: () -> Unit,
) {
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()

    OnboardingCompleteScreen2(
        state = state,
        onNextClick = { onFinishOnboarding() },
        onBackClick = { viewModel.processAction(OnboardingContract.Action.PreviousStep) },
    )
}

@Composable
fun OnboardingCompleteScreen2(
    state: OnboardingContract.State,
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
    currentStep: Int = 0,
    totalSteps: Int = 0,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OrbitTheme.colors.gray_900)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding(),
    ) {
        OnBoardingTopAppBar(
            currentStep = currentStep,
            totalSteps = totalSteps,
            onBackClick = onBackClick,
            showTopAppBarActions = false,
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
        ) {
            Spacer(modifier = Modifier.heightForScreenPercentage(0.05f))
            Text(
                text = stringResource(id = R.string.onboarding_completed_step1_subtitle),
                style = OrbitTheme.typography.body2Regular,
                color = OrbitTheme.colors.main,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
            Text(
                text = stringResource(id = R.string.onboarding_completed_step2_title),
                style = OrbitTheme.typography.heading1SemiBold,
                color = OrbitTheme.colors.white,
                modifier = Modifier
                    .fillMaxWidth()
                    .paddingForScreenPercentage(topPercentage = 0.0147f, bottomPercentage = 0.044f),
                textAlign = TextAlign.Center,
            )

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                LottieAnimation(
                    modifier = Modifier.wrapContentSize(),
                    resId = core.designsystem.R.raw.step3,
                    contentScale = ContentScale.FillWidth,
                    scaleXAdjustment = 0.85f,
                    scaleYAdjustment = 0.90f,
                )
                OrbitButton(
                    label = "시작하기",
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 10.dp)
                        .align(Alignment.BottomCenter),
                    onClick = onNextClick,
                    enabled = true,
                )
            }
        }
    }
}

@Composable
@Preview
fun OnboardingCompleteScreen2Preview() {
    OrbitTheme {
        OnboardingCompleteScreen2(
            state = OnboardingContract.State(),
            onNextClick = {},
            onBackClick = {},
        )
    }
}
