package com.example.sugarsaathi.util

object QuoteRepository {

    data class Quote(val en: String, val hi: String)

    val motivational = listOf(
        Quote("Every healthy meal is a step towards a longer, happier life.", "हर स्वस्थ भोजन एक लंबी, खुशहाल ज़िंदगी की ओर एक कदम है।"),
        Quote("You are stronger than your cravings. Believe in yourself.", "आप अपनी इच्छाओं से ज़्यादा मज़बूत हैं। खुद पर विश्वास रखें।"),
        Quote("Small consistent steps lead to big health changes.", "छोटे-छोटे कदम बड़े बदलाव लाते हैं।"),
        Quote("Your body is your most precious gift. Take care of it.", "आपका शरीर सबसे अनमोल उपहार है। इसकी देखभाल करें।"),
        Quote("Discipline today means freedom tomorrow.", "आज की अनुशासन कल की आज़ादी है।"),
        Quote("Healthy eating is not a diet, it is a lifestyle.", "स्वस्थ खाना कोई डाइट नहीं, यह एक जीवनशैली है।"),
        Quote("Every sunrise brings a new chance to make better choices.", "हर सुबह बेहतर चुनाव करने का नया मौका लाती है।"),
        Quote("Your health is an investment, not an expense.", "आपका स्वास्थ्य एक निवेश है, खर्च नहीं।"),
        Quote("Progress, not perfection, is the goal.", "लक्ष्य सिद्धि नहीं, प्रगति है।"),
        Quote("The secret of good health is to eat wisely and move often.", "अच्छे स्वास्थ्य का रहस्य है समझदारी से खाना और अक्सर चलना।"),
        Quote("Take care of your body. It is the only place you have to live.", "अपने शरीर की देखभाल करें। यही एकमात्र जगह है जहाँ आपको रहना है।"),
        Quote("One good choice at a time — that is how lasting change happens.", "एक समय में एक अच्छा चुनाव — इसी तरह स्थायी बदलाव होता है।"),
        Quote("Managing diabetes is not a burden, it is an act of love for yourself.", "मधुमेह का प्रबंधन बोझ नहीं, यह अपने प्रति प्रेम का कार्य है।"),
        Quote("Good health starts with what is on your plate.", "अच्छा स्वास्थ्य आपकी थाली से शुरू होता है।"),
        Quote("You have the power to change your health story.", "आपके पास अपनी स्वास्थ्य कहानी बदलने की शक्ति है।"),
        Quote("Each day is a fresh start — make it count.", "हर दिन एक नई शुरुआत है — इसे सार्थक बनाएं।"),
        Quote("Consistency beats intensity every single time.", "निरंतरता हमेशा तीव्रता से जीतती है।"),
        Quote("A healthy outside starts from the inside.", "एक स्वस्थ बाहरी शरीर अंदर से शुरू होता है।"),
        Quote("Your future self will thank you for the choices you make today.", "आपका भविष्य का स्व आज के आपके चुनावों के लिए धन्यवाद करेगा।"),
        Quote("Eat better, feel better, live better.", "बेहतर खाएं, बेहतर महसूस करें, बेहतर जीएं।"),
        Quote("You are capable of amazing things when you commit to your health.", "जब आप अपने स्वास्थ्य के प्रति प्रतिबद्ध होते हैं तो आप अद्भुत काम कर सकते हैं।"),
        Quote("Challenges make you stronger. Keep going.", "चुनौतियाँ आपको मज़बूत बनाती हैं। चलते रहिए।"),
        Quote("Good habits built today are your shield for tomorrow.", "आज बनाई अच्छी आदतें कल आपकी ढाल हैं।"),
        Quote("Health is not about being perfect. It is about making better choices.", "स्वास्थ्य सही होने के बारे में नहीं, बेहतर चुनाव करने के बारे में है।"),
        Quote("Believe in the power of one more good day.", "एक और अच्छे दिन की शक्ति पर विश्वास करें।"),
        Quote("Your determination is your greatest medicine.", "आपका संकल्प आपकी सबसे बड़ी दवा है।"),
        Quote("Be patient with yourself. Real change takes time.", "अपने साथ धैर्य रखें। वास्तविक बदलाव में समय लगता है।"),
        Quote("Every meal is an opportunity to nourish your body.", "हर भोजन आपके शरीर को पोषण देने का अवसर है।"),
        Quote("Stay strong. Your health journey is worth it.", "मज़बूत रहें। आपकी स्वास्थ्य यात्रा इसके लायक है।"),
        Quote("The best project you will ever work on is yourself.", "आप जिस पर काम करेंगे वह सबसे अच्छा प्रोजेक्ट आप खुद हैं।")
    )

    val congratulatory = listOf(
        Quote("Fantastic! You took charge of your health today!", "शानदार! आपने आज अपने स्वास्थ्य की ज़िम्मेदारी ली!"),
        Quote("That is the spirit! Another great day under your belt!", "यही जज़्बा है! एक और बेहतरीन दिन आपके नाम!"),
        Quote("You did it! Your body is grateful for your commitment.", "आपने किया! आपका शरीर आपकी प्रतिबद्धता के लिए कृतज्ञ है।"),
        Quote("Well done! Every good day builds a healthier you.", "शाबाश! हर अच्छा दिन एक स्वस्थ आपका निर्माण करता है।"),
        Quote("You are on fire! Keep this amazing momentum going!", "आप शानदार हैं! इस अद्भुत गति को जारी रखें!"),
        Quote("Excellent choice today! Your future self is smiling.", "आज एक उत्कृष्ट चुनाव! आपका भविष्य का स्व मुस्कुरा रहा है।"),
        Quote("Champion! You showed up for your health today.", "चैंपियन! आप आज अपने स्वास्थ्य के लिए आए।"),
        Quote("Brilliant! Consistent choices lead to extraordinary results.", "बेहतरीन! निरंतर चुनाव असाधारण परिणाम देते हैं।"),
        Quote("Way to go! You are writing a beautiful health story.", "बढ़िया! आप एक सुंदर स्वास्थ्य कहानी लिख रहे हैं।"),
        Quote("You should be proud. That was not easy, but you did it!", "आपको गर्व होना चाहिए। यह आसान नहीं था, लेकिन आपने किया!"),
        Quote("Another day, another victory for your health!", "एक और दिन, आपके स्वास्थ्य की एक और जीत!"),
        Quote("Incredible! You are unstoppable when you put your mind to it.", "अविश्वसनीय! जब आप ठान लेते हैं तो आपको कोई नहीं रोक सकता।"),
        Quote("Keep it up! Small wins today lead to big results tomorrow.", "जारी रखें! आज की छोटी जीत कल बड़े नतीजे देती है।"),
        Quote("You are a role model. Your family is proud of you.", "आप एक आदर्श हैं। आपका परिवार आप पर गर्व करता है।"),
        Quote("Hero of the day! Your health is your superpower.", "दिन के नायक! आपका स्वास्थ्य आपकी महाशक्ति है।"),
        Quote("Outstanding! You are building something truly special — good health.", "असाधारण! आप कुछ बहुत खास बना रहे हैं — अच्छा स्वास्थ्य।"),
        Quote("Superb! One more brick in the wall of your strong health.", "शानदार! आपके मज़बूत स्वास्थ्य की दीवार में एक और ईंट।"),
        Quote("You are glowing with health today! Keep shining.", "आप आज स्वास्थ्य से चमक रहे हैं! चमकते रहें।"),
        Quote("Magnificent! Discipline is the bridge between goals and achievement.", "अद्भुत! अनुशासन लक्ष्यों और उपलब्धि के बीच का पुल है।"),
        Quote("You made it happen! This is what dedication looks like.", "आपने इसे संभव किया! यही समर्पण दिखता है।")
    )

    fun getDailyMotivational(dayOfYear: Int): Quote = motivational[dayOfYear % motivational.size]

    fun getRandomCongratulatoryQuote(): Quote = congratulatory.random()
}