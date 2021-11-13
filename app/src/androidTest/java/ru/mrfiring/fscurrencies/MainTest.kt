package ru.mrfiring.fscurrencies

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.agoda.kakao.common.utilities.getResourceString
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.*
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.mrfiring.fscurrencies.data.asDatabaseObject
import ru.mrfiring.fscurrencies.data.asDomainObject
import ru.mrfiring.fscurrencies.data.network.CurrenciesService
import ru.mrfiring.fscurrencies.domain.DomainCurrency
import ru.mrfiring.fscurrencies.presentation.MainActivity
import ru.mrfiring.fscurrencies.utils.KTestCase
import javax.inject.Inject


@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class MainTest : KTestCase() {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)


    @Inject
    lateinit var service: CurrenciesService

    private lateinit var currencies: List<DomainCurrency>

    @Before
    fun init() {
        hiltRule.inject()
        runBlocking {
            currencies = service.get()
                .valute
                .values
                .toList()
                .map {
                    it.asDatabaseObject(0)
                        .asDomainObject()
                }
        }
    }

    @Test
    fun checkLeftViews() {
        run {
            step("1. Left views are visible and have text") {
                MainScreen {
                    leftNameView {
                        isDisplayed()
                        hasText("RUB")
                    }
                    leftValueView {
                        isDisplayed()
                        hasText("1")
                    }
                }
            }
        }
    }


    @Test
    fun checkCurrenciesAreConvertedProperly() {
        val index = currencies.indexOfFirst {
            it.charCode == "USD"
        }
        run {
            step("1. Select USD in the list") {
                MainScreen {
                    currenciesList {
                        scrollTo(index)
                        childAt<MainScreen.CurrencyItem>(index) {
                            isVisible()
                            shortName.hasText(currencies[index].charCode)
                            click()
                        }
                    }
                }
            }

            step("2. Type 1000 and check conversion") {
                MainScreen {
                    rightNameView {
                        hasText("USD")
                    }
                    leftValueView {
                        clearText()
                        typeText("1000")
                    }
                    rightValueView {
                        hasText(
                            (1000 / currencies[index].getValuePerNominal()).toString()
                        )
                    }
                }
            }
        }

    }

    @Test
    fun checkCurrenciesAreDisplayedProperly() {
        run {
            step("1. Displayed not least than 5 currencies") {
                MainScreen {
                    currenciesList {
                        assertTrue(getSize() >= 5)
                    }
                }
            }

            step("2. Are all of the currencies displayed properly?") {
                checkCurrencies(currencies)
            }

        }
    }

    fun checkCurrencies(items: List<DomainCurrency>) {
        items.forEachIndexed { index, item ->
            MainScreen {
                currenciesList {
                    childAt<MainScreen.CurrencyItem>(index) {
                        shortName {
                            isDisplayed()
                            hasText(item.charCode)
                        }
                        name {
                            isDisplayed()
                        }
                        currentPrice {
                            isDisplayed()
                            hasText(
                                getResourceString(
                                    R.string.price_format
                                ).format(
                                    item.getValuePerNominal()
                                )
                            )
                        }
                        deltaPrice {
                            isDisplayed()
                            hasText(
                                getResourceString(
                                    R.string.price_delta_format
                                ).format(
                                    item.getDeltaPrice()
                                )
                            )
                        }
                    }
                }
            }
        }

    }

}