package com.example.data.model

object LocalHubs {
    val NARAYANGHAT = LocationHub(
        id = "narayanghat",
        nameEn = "Narayanghat / Bharatpur (Chitwan)",
        nameNp = "नारायणगढ / भरतपुर (चितवन)",
        centerLat = 27.6934,
        centerLng = 84.4300,
        defaultRadiusKm = 20.0,
        cityTariff = CityTariff(
            cityName = "Narayanghat",
            eRickshawBaseNpr = 30,
            eRickshawPerKmNpr = 14,
            autoRickshawBaseNpr = 45,
            autoRickshawPerKmNpr = 18,
            cargoBaseNpr = 50,
            cargoPerKmNpr = 22,
            tariffSource = "Narayanghat E-Rickshaw Operators Union Tariff"
        ),
        landmarks = listOf(
            LandmarkLocation("ng_1", "Lions Chowk, Narayanghat", "लायन्स चोक, नारायणगढ", 27.7018, 84.4312, "Chowk"),
            LandmarkLocation("ng_2", "Sahid Chowk, Narayanghat", "शहीद चोक, नारायणगढ", 27.6985, 84.4265, "Bazaar"),
            LandmarkLocation("ng_3", "Pulchowk Narayani River Bridge", "पुलचोक नारायणी पुल", 27.7050, 84.4180, "Bridge / Hub"),
            LandmarkLocation("ng_4", "Chaubiskothi Hospital Area", "चौबिसकोठी अस्पताल क्षेत्र", 27.6812, 84.4310, "Hospital"),
            LandmarkLocation("ng_5", "Bharatpur Central Bus Terminal", "भरतपुर केन्द्रीय बसपार्क", 27.6740, 84.4420, "Buspark"),
            LandmarkLocation("ng_6", "Birendra Multiple Campus", "वीरेन्द्र बहुमुखी क्याम्पस", 27.6890, 84.4380, "Campus"),
            LandmarkLocation("ng_7", "Hakim Chowk, Bharatpur", "हाकिम चोक, भरतपुर", 27.6780, 84.4390, "Chowk"),
            LandmarkLocation("ng_8", "Bhojad Bypass / Tole", "भोजड बाइपास / टोल", 27.7120, 84.4450, "Residential"),
            LandmarkLocation("ng_9", "Mangalpur Bazaar", "मंगलपुर बजार", 27.6520, 84.3720, "Bazaar"),
            LandmarkLocation("ng_10", "Tandi / Ratnanagar Chowk", "टाँडी / रत्ननगर चोक", 27.6180, 84.5200, "Commercial Hub")
        )
    )

    val BIRATNAGAR = LocationHub(
        id = "biratnagar",
        nameEn = "Biratnagar (Morang)",
        nameNp = "विराटनगर (मोरङ)",
        centerLat = 26.4525,
        centerLng = 87.2718,
        defaultRadiusKm = 20.0,
        cityTariff = CityTariff(
            cityName = "Biratnagar",
            eRickshawBaseNpr = 25,
            eRickshawPerKmNpr = 12,
            autoRickshawBaseNpr = 40,
            autoRickshawPerKmNpr = 16,
            cargoBaseNpr = 45,
            cargoPerKmNpr = 20,
            tariffSource = "Biratnagar Metropolitan E-Safari Committee Rates"
        ),
        landmarks = listOf(
            LandmarkLocation("brt_1", "Traffic Chowk, Main Road", "ट्राफिक चोक, मेनरोड", 26.4525, 87.2718, "Chowk"),
            LandmarkLocation("brt_2", "Rani Customs / Jogbani Border", "रानी भन्सार / जोगबनी नाका", 26.4020, 87.2760, "Border Hub"),
            LandmarkLocation("brt_3", "Roadcess Chowk", "रोडशेष चोक", 26.4680, 87.2720, "Hub"),
            LandmarkLocation("brt_4", "Bargachhi Chowk", "बरगाछी चोक", 26.4850, 87.2750, "Chowk"),
            LandmarkLocation("brt_5", "Biratnagar Airport Gate", "विराटनगर विमानस्थल गेट", 26.4830, 87.2640, "Airport"),
            LandmarkLocation("brt_6", "Nobel Medical College Hospital", "नोबेल मेडिकल कलेज", 26.4670, 87.3010, "Hospital"),
            LandmarkLocation("brt_7", "Koshi Hospital Chowk", "कोशी अस्पताल चोक", 26.4560, 87.2790, "Hospital"),
            LandmarkLocation("brt_8", "Tintolia Bazaar Area", "तीनटोलिया बजार क्षेत्र", 26.4620, 87.2680, "Bazaar")
        )
    )

    val KATHMANDU = LocationHub(
        id = "ktm_valley",
        nameEn = "Kathmandu Valley (KTM/Patan/Bhaktapur)",
        nameNp = "काठमाडौँ उपत्यका",
        centerLat = 27.7007,
        centerLng = 85.3160,
        defaultRadiusKm = 20.0,
        cityTariff = CityTariff(
            cityName = "Kathmandu",
            eRickshawBaseNpr = 35,
            eRickshawPerKmNpr = 18,
            autoRickshawBaseNpr = 50,
            autoRickshawPerKmNpr = 22,
            cargoBaseNpr = 65,
            cargoPerKmNpr = 28,
            tariffSource = "Kathmandu Valley Safaa Tempo & Auto Standard Tariff"
        ),
        landmarks = listOf(
            LandmarkLocation("ktm_1", "Ratnapark / Safaa Tempo Stand", "रत्नपार्क सफा टेम्पो स्टेसन", 27.7058, 85.3148, "Tempo Stand"),
            LandmarkLocation("ktm_2", "Lagankhel Buspark, Lalitpur", "लगनखेल बसपार्क, ललितपुर", 27.6672, 85.3218, "Buspark"),
            LandmarkLocation("ktm_3", "Kalanki Chowk", "कलंकी चोक", 27.6937, 85.2818, "Chowk"),
            LandmarkLocation("ktm_4", "Koteshwor Chowk", "कोटेश्वर चोक", 27.6766, 85.3499, "Chowk"),
            LandmarkLocation("ktm_5", "Gongabu Naya Buspark", "गोंगबु नयाँ बसपार्क", 27.7348, 85.3117, "Buspark"),
            LandmarkLocation("ktm_6", "Baneshwor / Parliament Hub", "बानेश्वर चोक", 27.6915, 85.3421, "Commercial"),
            LandmarkLocation("ktm_7", "Patan Durbar Square Chowk", "पाटन दरबार स्क्वायर", 27.6744, 85.3260, "Cultural"),
            LandmarkLocation("ktm_8", "Suryabinayak, Bhaktapur", "सूर्यविनायक, भक्तपुर", 27.6698, 85.4285, "Hub")
        )
    )

    val POKHARA = LocationHub(
        id = "pokhara",
        nameEn = "Pokhara Valley",
        nameNp = "पोखरा उपत्यका",
        centerLat = 28.2096,
        centerLng = 83.9856,
        defaultRadiusKm = 20.0,
        cityTariff = CityTariff(
            cityName = "Pokhara",
            eRickshawBaseNpr = 35,
            eRickshawPerKmNpr = 16,
            autoRickshawBaseNpr = 50,
            autoRickshawPerKmNpr = 20,
            cargoBaseNpr = 60,
            cargoPerKmNpr = 25,
            tariffSource = "Pokhara Tourism Corridor & City Auto Standard"
        ),
        landmarks = listOf(
            LandmarkLocation("pkr_1", "Prithvi Chowk Transport Hub", "पृथ्वी चोक", 28.2105, 83.9892, "Chowk"),
            LandmarkLocation("pkr_2", "Lakeside Barahi Chowk", "लेकसाइड बाराही चोक", 28.2090, 83.9580, "Tourism Hub"),
            LandmarkLocation("pkr_3", "Mahendrapool Bazaar", "महेन्द्रपुल बजार", 28.2285, 83.9870, "Bazaar"),
            LandmarkLocation("pkr_4", "Birauta Chowk", "बिरौटा चोक", 28.1880, 83.9720, "Residential"),
            LandmarkLocation("pkr_5", "Baglung Buspark", "बाग्लुङ बसपार्क", 28.2420, 83.9810, "Buspark")
        )
    )

    val BUTWAL = LocationHub(
        id = "butwal",
        nameEn = "Butwal - Bhairahawa Corridor",
        nameNp = "बुटवल - भैरहवा",
        centerLat = 27.7006,
        centerLng = 83.4485,
        defaultRadiusKm = 20.0,
        cityTariff = CityTariff(
            cityName = "Butwal",
            eRickshawBaseNpr = 30,
            eRickshawPerKmNpr = 14,
            autoRickshawBaseNpr = 45,
            autoRickshawPerKmNpr = 18,
            cargoBaseNpr = 50,
            cargoPerKmNpr = 22,
            tariffSource = "Rupandehi Auto Rickshaw Association Rates"
        ),
        landmarks = listOf(
            LandmarkLocation("btl_1", "Traffic Chowk, Butwal", "ट्राफिक चोक, बुटवल", 27.7006, 83.4580, "Chowk"),
            LandmarkLocation("btl_2", "Golpark, Butwal", "गोलपार्क, बुटवल", 27.7120, 83.4620, "Hub"),
            LandmarkLocation("btl_3", "Manigram Chowk", "मणिग्राम चोक", 27.6320, 83.4690, "Chowk"),
            LandmarkLocation("btl_4", "Buddha Chowk, Bhairahawa", "बुद्ध चोक, भैरहवा", 27.5050, 83.4500, "Commercial"),
            LandmarkLocation("btl_5", "Belahiya Nepal-India Border", "बेलहिया नाका", 27.4680, 83.4610, "Border Hub")
        )
    )

    val NEPALGUNJ = LocationHub(
        id = "nepalgunj",
        nameEn = "Nepalgunj (Banke)",
        nameNp = "नेपालगन्ज (बाँके)",
        centerLat = 28.0500,
        centerLng = 81.6167,
        defaultRadiusKm = 20.0,
        cityTariff = CityTariff(
            cityName = "Nepalgunj",
            eRickshawBaseNpr = 25,
            eRickshawPerKmNpr = 12,
            autoRickshawBaseNpr = 40,
            autoRickshawPerKmNpr = 16,
            cargoBaseNpr = 45,
            cargoPerKmNpr = 20,
            tariffSource = "Nepalgunj City E-Safari Tariff"
        ),
        landmarks = listOf(
            LandmarkLocation("npj_1", "Birendra Chowk / BP Chowk", "विरेन्द्र चोक / बिपी चोक", 28.0520, 81.6190, "Chowk"),
            LandmarkLocation("npj_2", "Dhamboji Chowk", "धम्बोझी चोक", 28.0580, 81.6180, "Commercial"),
            LandmarkLocation("npj_3", "Ranjha Airport Gate", "राँझा विमानस्थल", 28.1020, 81.6670, "Airport"),
            LandmarkLocation("npj_4", "Rupaidiha / Jamunaha Border", "जमुनाहा नाका", 28.0120, 81.6150, "Border")
        )
    )

    val ALL_HUBS = listOf(NARAYANGHAT, BIRATNAGAR, KATHMANDU, POKHARA, BUTWAL, NEPALGUNJ)
}
