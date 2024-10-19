package com.example.authentication.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.feature.authentication.R
import com.example.authentication.domain.model.EmailAuthRequest
import com.example.authentication.model.AuthType
import com.example.designsystem.components.AppIcon
import com.example.designsystem.components.AppOutlinedButton
import com.example.designsystem.components.ButtonState
import com.example.designsystem.components.TitleSlot
import com.example.designsystem.components.VerticalSpacer
import com.example.designsystem.components.textfield.AppTextField
import com.example.designsystem.components.textfield.rememberEmailInputState
import com.example.designsystem.components.textfield.rememberPasswordInputState
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.theme.MeterAppTheme
import com.example.designsystem.theme.ValueChanged
import com.example.designsystem.theme.VoidCallback

@Composable
internal fun AuthScreenSlot(
    modifier: Modifier = Modifier,
    authType: AuthType,
    onAuthChange: ValueChanged<AuthType>,
    onAuthRequest: ValueChanged<EmailAuthRequest>
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        //Icon
        AppIcon(
            modifier = Modifier
                .size(180.dp),
            icon = authType.getIcon(),
            tint = AppTheme.colors.brand
        )

        AuthenticationContent(
            title = authType.toString(),
            onAuthRequest = onAuthRequest
        )

        BottomContent(
            authType = authType,
            onAuthChange = onAuthChange
        )
    }
}

@Composable
private fun AuthenticationContent(
    modifier: Modifier = Modifier,
    title: String,
    onAuthRequest: ValueChanged<EmailAuthRequest>
) {

    val emailState = rememberEmailInputState(hint = stringResource(R.string.email))
    val passwordState = rememberPasswordInputState(hint = stringResource(R.string.password))

    TitleSlot(
        modifier = modifier.fillMaxWidth(),
        title = title,
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            //Email
            AppTextField(state = emailState)

            VerticalSpacer(height = AppTheme.padding.medium)

            //Password
            AppTextField(state = passwordState)

            VerticalSpacer(height = 62.dp)

            AppOutlinedButton(
                text = title,
                buttonState = if (emailState.hasError || passwordState.hasError) ButtonState.DISABLED
                else ButtonState.ACTIVE,
                onClick = {
                    onAuthRequest(
                        EmailAuthRequest(
                            email = emailState.text,
                            password = passwordState.text
                        )
                    )
                }
            )
        }
    }
}


@Composable
private fun BottomContent(
    modifier: Modifier = Modifier,
    authType: AuthType,
    onAuthChange: ValueChanged<AuthType>
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                val type = if (authType.isLogin())
                    AuthType.SIGNUP_WITH_EMAIL
                else AuthType.LOGIN_WITH_EMAIL
                onAuthChange(type)
            },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val msgText = if (authType.isLogin()) {
            stringResource(R.string.already_have_an_account)
        } else {
            stringResource(R.string.don_t_have_an_account)
        }

        val titleText = if (authType.isLogin()) {
            stringResource(R.string.signup)
        } else {
            stringResource(R.string.login)
        }

        Text(
            modifier = Modifier.padding(end = 4.dp),
            text = msgText,
            style = MaterialTheme.typography.labelSmall,
            color = AppTheme.colors.textHighlighted
        )

        Text(
            text = titleText,
            style = MaterialTheme.typography.labelSmall,
            color = AppTheme.colors.brand
        )
    }
}

@Preview
@Composable
private fun PreviewAuthScreenSlot() {
    MeterAppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
        ) {
            AuthScreenSlot(
                authType = AuthType.LOGIN_WITH_EMAIL,
                onAuthChange = { }
            ) {

            }
        }
    }
}