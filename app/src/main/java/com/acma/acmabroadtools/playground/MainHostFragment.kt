package com.acma.acmabroadtools.playground

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.acma.acmabroadtools.R
import com.acma.acmabroadtools.databinding.FragmentMainHostBinding
import com.acma.broad.helper.AdSdkInitializer
import com.acma.broad.helper.interstitial.InterstitialHelper
import com.acma.broad.helper.rewarded.RewardedAdHelper
import com.acma.broad.helper.rewarded.RewardedInterstitialHelper

class MainHostFragment : Fragment() {

    private var _binding: FragmentMainHostBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainHostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.apply {
            xmlAdsBtn.setOnClickListener {
                findNavController().navigate(R.id.xmlAdsHostFragment)
            }
            ComposeAdsBtn.setOnClickListener {
                findNavController().navigate(R.id.composeAdsHostFragment)
            }

            IntersitialAdBtn.setOnClickListener {
                InterstitialHelper.showAd(requireActivity()) {
                    Log.d("AdDemo", "onAdClosed Interstitial")
                }
            }
            RewardedInterstitialAdsBtn.setOnClickListener {
                RewardedInterstitialHelper.showAd(requireActivity(),
                    onAdClosed = { Log.d("AdDemo", "Ad was dismissed.") },
                    onUserEarnedReward = { rewardAmount, rewardType ->
                        Log.d("AdDemo", "User earned $rewardAmount $rewardType.")
                    }
                )
            }
            RewardedAdsBtn.setOnClickListener {
                RewardedAdHelper.showAd(requireActivity(),
                    onAdClosed = { Log.d("AdDemo", "Ad was dismissed.") },
                    onUserEarnedReward = { rewardAmount, rewardType ->
                        Log.d("AdDemo", "User earned $rewardAmount $rewardType.")
                    }
                )
            }
            RequestConsentBtn.setOnClickListener {
                AdSdkInitializer.handleConsent(requireActivity()) { consentGiven ->
                    if (consentGiven) Log.d("AdDemo", "User granted consent.")
                    else Log.d("AdDemo", "User denied consent.")
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}