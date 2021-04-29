package ru.mrfiring.fscurrencies

import android.view.View
import com.agoda.kakao.edit.KEditText
import com.agoda.kakao.recycler.KRecyclerItem
import com.agoda.kakao.recycler.KRecyclerView
import com.agoda.kakao.screen.Screen
import com.agoda.kakao.text.KTextView
import org.hamcrest.Matcher

object MainScreen: Screen<MainScreen>() {

    val leftNameView = KTextView {withId(R.id.leftCurrencyName)}
    val leftValueView = KEditText {withId(R.id.leftCurrencyValue)}

    val rightNameView = KTextView {withId(R.id.rightCurrencyName)}
    val rightValueView = KEditText {withId(R.id.rightCurrencyValue)}


    val currenciesList = KRecyclerView(
        builder = {withId(R.id.currencyList)},
        itemTypeBuilder = {itemType(::CurrencyItem)}
    )

    class CurrencyItem(parent: Matcher<View>): KRecyclerItem<CurrencyItem>(parent){
        val name = KTextView(parent)         {withId(R.id.currencyName)}
        val shortName = KTextView(parent)    {withId(R.id.currencyShortName)}
        val currentPrice = KTextView(parent) {withId(R.id.currentPrice)}
        val deltaPrice = KTextView(parent)   {withId(R.id.deltaPrice)}
    }

}