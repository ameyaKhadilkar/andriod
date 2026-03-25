package com.example.sugarsaathi.util

import java.time.LocalDate

data class Festival(
    val nameEn: String,
    val nameHi: String,
    val greetingEn: String,
    val greetingHi: String,
    val tipEn: String,
    val tipHi: String,
    val emoji: String
)

object FestivalHelper {

    fun getFestivalForDate(date: LocalDate): Festival? {
        val month = date.monthValue
        val day = date.dayOfMonth
        // Approximate fixed dates (Diwali/Holi float — covered by a range)
        return when {
            // Diwali: approx Oct 20 – Nov 15 window (varies yearly)
            month == 10 && day in 20..31 -> diwali
            month == 11 && day in 1..15  -> diwali
            // Holi: approx Feb 25 – Mar 15
            month == 2 && day in 25..28  -> holi
            month == 3 && day in 1..15   -> holi
            // Navratri: approx Oct 1–14
            month == 10 && day in 1..14  -> navratri
            // Eid: approx Apr 8–10 (Eid-ul-Fitr) — rough
            month == 4 && day in 8..10   -> eid
            // Raksha Bandhan: approx Aug 19–23
            month == 8 && day in 19..23  -> rakshaBandhan
            // New Year
            month == 1 && day == 1       -> newYear
            // Christmas
            month == 12 && day == 25     -> christmas
            else -> null
        }
    }

    private val diwali = Festival(
        nameEn = "Diwali",
        nameHi = "दिवाली",
        greetingEn = "Happy Diwali! May this festival of lights brighten your health journey too!",
        greetingHi = "दिवाली मुबारक! रोशनी का यह त्योहार आपके स्वास्थ्य की राह भी रोशन करे!",
        tipEn = "Festival tip: Enjoy 1-2 sweets mindfully. Have a protein-rich meal before sweets to reduce sugar spikes.",
        tipHi = "त्योहार की सलाह: 1-2 मिठाइयाँ ध्यान से खाएं। मिठाई से पहले प्रोटीन युक्त भोजन लें ताकि शुगर कम बढ़े।",
        emoji = "🪔"
    )

    private val holi = Festival(
        nameEn = "Holi",
        nameHi = "होली",
        greetingEn = "Happy Holi! Celebrate with colors and keep your plate colorful with veggies too!",
        greetingHi = "होली मुबारक! रंगों से खेलें और थाली में भी सब्ज़ियों के रंग भरें!",
        tipEn = "Festival tip: Opt for thandai with less sugar. Fresh fruits are a great festive treat!",
        tipHi = "त्योहार की सलाह: कम चीनी वाली ठंडाई चुनें। ताज़े फल एक बेहतरीन उत्सव का व्यंजन हैं!",
        emoji = "🎨"
    )

    private val navratri = Festival(
        nameEn = "Navratri",
        nameHi = "नवरात्रि",
        greetingEn = "Happy Navratri! Fasting wisely during Navratri can be good for blood sugar management.",
        greetingHi = "नवरात्रि की शुभकामनाएं! नवरात्रि में समझदारी से व्रत रखना ब्लड शुगर प्रबंधन के लिए अच्छा हो सकता है।",
        tipEn = "Festival tip: Include kuttu roti, singhara flour and milk — they have a lower glycemic index.",
        tipHi = "त्योहार की सलाह: कुट्टू की रोटी, सिंघाड़े का आटा और दूध शामिल करें — इनका ग्लाइसेमिक इंडेक्स कम होता है।",
        emoji = "🪷"
    )

    private val eid = Festival(
        nameEn = "Eid Mubarak",
        nameHi = "ईद मुबारक",
        greetingEn = "Eid Mubarak! May Allah bless you with good health and happiness.",
        greetingHi = "ईद मुबारक! अल्लाह आपको अच्छे स्वास्थ्य और खुशियाँ दें।",
        tipEn = "Festival tip: Seviyan can spike sugar. Enjoy a small portion and balance with a protein-rich meal.",
        tipHi = "त्योहार की सलाह: सेवइयाँ शुगर बढ़ा सकती हैं। छोटी मात्रा लें और प्रोटीन युक्त भोजन से संतुलन करें।",
        emoji = "🌙"
    )

    private val rakshaBandhan = Festival(
        nameEn = "Raksha Bandhan",
        nameHi = "रक्षाबंधन",
        greetingEn = "Happy Raksha Bandhan! The best gift to your family is staying healthy.",
        greetingHi = "रक्षाबंधन की शुभकामनाएं! परिवार को सबसे बड़ा तोहफा है आपका स्वस्थ रहना।",
        tipEn = "Festival tip: Choose dark chocolate or dry fruit sweets over traditional mithai for a healthier celebration.",
        tipHi = "त्योहार की सलाह: पारंपरिक मिठाई की जगह डार्क चॉकलेट या ड्राई फ्रूट स्वीट्स चुनें।",
        emoji = "🪢"
    )

    private val newYear = Festival(
        nameEn = "Happy New Year",
        nameHi = "नया साल मुबारक",
        greetingEn = "Happy New Year! A fresh year, a fresh commitment to your health. You have got this!",
        greetingHi = "नया साल मुबारक! नया साल, स्वास्थ्य के प्रति नई प्रतिबद्धता। आप यह कर सकते हैं!",
        tipEn = "New Year tip: Set one simple health goal for this year and track it here every day.",
        tipHi = "नया साल की सलाह: इस साल के लिए एक सरल स्वास्थ्य लक्ष्य तय करें और यहाँ हर दिन ट्रैक करें।",
        emoji = "🎆"
    )

    private val christmas = Festival(
        nameEn = "Merry Christmas",
        nameHi = "क्रिसमस की शुभकामनाएं",
        greetingEn = "Merry Christmas! Celebrate with joy and keep your health resolutions strong.",
        greetingHi = "क्रिसमस की शुभकामनाएं! खुशी से मनाएं और अपने स्वास्थ्य संकल्पों को मज़बूत रखें।",
        tipEn = "Festival tip: Enjoy the feast mindfully. Fill half your plate with veggies before the treats.",
        tipHi = "त्योहार की सलाह: भोजन को ध्यान से खाएं। मिठाइयों से पहले आधी थाली सब्ज़ियों से भरें।",
        emoji = "🎄"
    )
}