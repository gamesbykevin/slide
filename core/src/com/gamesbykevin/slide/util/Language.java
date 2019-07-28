package com.gamesbykevin.slide.util;

public class Language {

    //languages commented out here we don't have the character set for
    public enum Languages {

        Afrikaans("Afrikaans", "af", ""),
        Albanian("Albanian", "sq", ""),
        //Amharic("Amharic", "am", ""),
        //Arabic("Arabic", "ar", ""),
        //Armenian("Armenian", "hy", ""),
        Azerbaijani("Azerbaijani", "az", ""),
        Basque("Basque", "eu", ""),
        //Belarusian("Belarusian", "be", ""),
        //Bengali("Bengali", "bn", ""),
        Bosnian("Bosnian", "bs", ""),
        //Bulgarian("Bulgarian", "bg", ""),
        Catalan("Catalan", "ca", ""),
        Cebuano("Cebuano", "ceb", ""),
        //ChineseSimplified("Chinese (Simplified)", "zh", "CN"),
        //ChineseTraditional("Chinese (Traditional)", "zh", "TW"),
        Corsican("Corsican", "co", ""),
        Croatian("Croatian", "hr", ""),
        Czech("Czech", "cs", ""),
        Danish("Danish", "da", ""),
        Dutch("Dutch", "nl", ""),
        English("English", "en", ""),
        Esperanto("Esperanto", "eo", ""),
        Estonian("Estonian", "et", ""),
        Finnish("Finnish", "fi", ""),
        French("French", "fr", ""),
        Frisian("Frisian", "fy", ""),
        Galician("Galician", "gl", ""),
        //Georgian("Georgian", "ka", ""),
        German("German", "de", ""),
        //Greek("Greek", "el", ""),
        //Gujarati("Gujarati", "gu", ""),
        Haitian("Haitian Creole", "ht", ""),
        Hausa("Hausa", "ha", ""),
        Hawaiian("Hawaiian", "haw", "639"),
        //Hebrew("Hebrew", "he", ""),
        //Hindi("Hindi", "hi", ""),
        Hmong("Hmong", "hmn", "639"),
        Hungarian("Hungarian", "hu", ""),
        Icelandic("Icelandic", "is", ""),
        Igbo("Igbo", "ig", ""),
        Indonesian("Indonesian", "id", ""),
        Irish("Irish", "ga", ""),
        Italian("Italian", "it", ""),
        //Japanese("Japanese", "ja", ""),
        Javanese("Javanese", "jw", ""),
        //Kannada("Kannada", "kn", ""),
        //Kazakh("Kazakh", "kk", ""),
        //Khmer("Khmer", "km", ""),
        //Korean("Korean", "ko", ""),
        Kurdish("Kurdish", "ku", ""),
        //Kyrgyz("Kyrgyz", "ky", ""),
        //Lao("Lao", "lo", ""),
        Latin("Latin", "la", ""),
        Latvian("Latvian", "lv", ""),
        Lithuanian("Lithuanian", "lt", ""),
        Luxembourgish("Luxembourgish", "lb", ""),
        //Macedonian("Macedonian", "mk", ""),
        Malagasy("Malagasy", "mg", ""),
        Malay("Malay", "ms", ""),
        //Malayalam("Malayalam", "ml", ""),
        Maltese("Maltese", "mt", ""),
        Maori("Maori", "mi", ""),
        //Marathi("Marathi", "mr", ""),
        //Mongolian("Mongolian", "mn", ""),
        //Myanmar("Myanmar (Burmese)", "my", ""),
        //Nepali("Nepali", "ne", ""),
        Norwegian("Norwegian", "no", ""),
        Nyanja("Nyanja (Chichewa)", "ny", ""),
        //Pashto("Pashto", "ps", ""),
        //Persian("Persian", "fa", ""),
        Polish("Polish", "pl", ""),
        Portuguese("Portuguese", "pt", ""),
        //Punjabi("Punjabi", "pa", ""),
        Romanian("Romanian", "ro", ""),
        //Russian("Russian", "ru", ""),
        Samoan("Samoan", "sm", ""),
        ScotsGaelic("Scots Gaelic", "gd", ""),
        //Serbian("Serbian", "sr", ""),
        Sesotho("Sesotho", "st", ""),
        Shona("Shona", "sn", ""),
        //Sindhi("Sindhi", "sd", ""),
        //Sinhala("Sinhala (Sinhalese)", "si", ""),
        Slovak("Slovak", "sk", ""),
        Slovenian("Slovenian", "sl", ""),
        Somali("Somali", "so", ""),
        Spanish("Spanish", "es", ""),
        Sundanese("Sundanese", "su", ""),
        Swahili("Swahili", "sw", ""),
        Swedish("Swedish", "sv", ""),
        Tagalog("Tagalog (Filipino)", "tl", ""),
        //Tajik("Tajik", "tg", ""),
        //Tamil("Tamil", "ta", ""),
        //Telugu("Telugu", "te", ""),
        //Thai("Thai", "th", ""),
        Turkish("Turkish", "tr", ""),
        //Ukrainian("Ukrainian", "uk", ""),
        //Urdu("Urdu", "ur", ""),
        Uzbek("Uzbek", "uz", ""),
        Vietnamese("Vietnamese", "vi", ""),
        Welsh("Welsh", "cy", ""),
        Xhosa("Xhosa", "xh", ""),
        //Yiddish("Yiddish", "yi", ""),
        Yoruba("Yoruba", "yo", ""),
        Zulu("Zulu", "zu", "");

        private final String desc;
        private final String languageCode;
        private final String countryCode;

        private Languages(String desc, String languageCode, String countryCode) {
            this.desc = desc;
            this.languageCode = languageCode;
            this.countryCode = countryCode;
        }

        public String getDesc() {
            return this.desc;
        }

        public String getLanguageCode() {
            return this.languageCode;
        }

        public String getCountryCode() {
            return this.countryCode;
        }
    }
}
