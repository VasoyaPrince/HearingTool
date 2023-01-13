package glory.hearing.tool.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import glory.hearing.tool.adepter.ItemAdapter
import glory.hearing.tool.model.Item
import glory.hearing.tool.model.SliderTransformer
import com.pesonal.adsdk.AppManage
import glory.hearing.tool.R
import glory.hearing.tool.databinding.ActivityHearingFactsBinding


class HearingFactsActivity : AppCompatActivity() {
    lateinit var binding: ActivityHearingFactsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHearingFactsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        binding.toolbarInclude.back.setOnClickListener {
            AppManage.getInstance(this@HearingFactsActivity).showInterstitialAd(
                this@HearingFactsActivity, {
                    startActivity(Intent(this@HearingFactsActivity, MainActivity::class.java))
                    finish()
                }, "", AppManage.app_mainClickCntSwAd
            )
        }

        //loadInterstitialAd
        AppManage.getInstance(this).loadInterstitialAd(
            this, AppManage.ADMOB_I[0], AppManage.FACEBOOK_I[0]
        )
        //banner ads
        AppManage.getInstance(this).showNativeBanner(
            binding.linearLayout, AppManage.ADMOB_I[0], AppManage.FACEBOOK_I[0]
        )
       AppManage.getInstance(this@HearingFactsActivity).showNativeBanner(binding.linearLayout)


        binding.viewpager.apply {
            adapter = ItemAdapter(this@HearingFactsActivity, listOfPhotos(),binding.viewpager)
            offscreenPageLimit = 4
            setPageTransformer(SliderTransformer(4))
                clipToPadding = false
                clipChildren = false

        }
    }

    private fun listOfPhotos(): ArrayList<Item> {

        val photos = ArrayList<Item>()

        photos.add(
            Item(
                R.drawable.img1,
                "Hearing Music",
                "studies have shown that we can \n hear the music better on our left \n side"
            )
        )

        photos.add(
            Item(
                R.drawable.img_1,
                "Ears are always working",
                "Ears never gets any day off while sleeping, conscious or unconscious"
            )
        )

        photos.add(
            Item(
                R.drawable.img3,
                "AutoCleaning",
                "Ear pushes the earwax out when it is needed, so there is no need to remove except in some abnormal"
            )
        )

        photos.add(
            Item(
                R.drawable.homeremedies,
                "Household Remedies",
                "Increase consumption of green leafy vegetable containing omega 3 fatty acid."
            )
        )

        return photos
    }

    override fun onBackPressed() {
        AppManage.getInstance(this@HearingFactsActivity).showInterstitialAd(
            this@HearingFactsActivity, {
                super.onBackPressed()
            }, "", AppManage.app_innerClickCntSwAd
        )
    }
}

class CircularViewPagerHandler(private val mViewPager: ViewPager) : OnPageChangeListener {
    private var mCurrentPosition = 0
    private var mScrollState = 0
    override fun onPageSelected(position: Int) {
        mCurrentPosition = position
    }

    override fun onPageScrollStateChanged(state: Int) {
        handleScrollState(state)
        mScrollState = state
    }

    private fun handleScrollState(state: Int) {
        if (state == ViewPager.SCROLL_STATE_IDLE && mScrollState == ViewPager.SCROLL_STATE_DRAGGING) {
            setNextItemIfNeeded()
        }
    }

    private fun setNextItemIfNeeded() {
        if (!isScrollStateSettling) {
            handleSetNextItem()
        }
    }

    private val isScrollStateSettling: Boolean
        get() = mScrollState == ViewPager.SCROLL_STATE_SETTLING

    private fun handleSetNextItem() {
        val lastPosition = mViewPager.adapter!!.count - 1
        if (mCurrentPosition == 0) {
            mViewPager.setCurrentItem(lastPosition, true)
        } else if (mCurrentPosition == lastPosition) {
            mViewPager.setCurrentItem(0, true)
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}


}
