package edtech.afrilingo.dataloader;

import java.util.HashMap;
import java.util.Map;

/**
 * Helper class that provides language-specific content for the data loader.
 * This class contains vocabulary, phrases, and other content in Kinyarwanda, Kiswahili, and English.
 */
public class LanguageContentHelper {

    /**
     * Get greeting phrases in different languages.
     * @return Map of language code to greeting phrases
     */
    public static Map<String, String[]> getGreetings() {
        Map<String, String[]> greetings = new HashMap<>();
        
        // Kinyarwanda greetings
        greetings.put("RW", new String[] {
            "Muraho", // Hello (to multiple people)
            "Uraho", // Hello (to one person)
            "Mwaramutse", // Good morning
            "Mwiriwe", // Good afternoon/evening
            "Amakuru?", // How are you?
            "Ni meza", // I'm fine
            "Murakoze", // Thank you
            "Yego", // Yes
            "Oya", // No
            "Murabeho" // Goodbye
        });
        
        // Kiswahili greetings
        greetings.put("SW", new String[] {
            "Jambo", // Hello
            "Habari?", // How are you?
            "Habari gani?", // How are you? (more casual)
            "Nzuri", // Good/fine
            "Hujambo", // How are you? (formal)
            "Sijambo", // I am fine
            "Asante", // Thank you
            "Ndio", // Yes
            "Hapana", // No
            "Kwa heri" // Goodbye
        });
        
        // English greetings
        greetings.put("EN", new String[] {
            "Hello",
            "Good morning",
            "Good afternoon",
            "Good evening",
            "How are you?",
            "I'm fine, thank you",
            "Thank you",
            "Yes",
            "No",
            "Goodbye"
        });
        
        return greetings;
    }
    
    /**
     * Get number words in different languages.
     * @return Map of language code to number words (1-10)
     */
    public static Map<String, String[]> getNumbers() {
        Map<String, String[]> numbers = new HashMap<>();
        
        // Kinyarwanda numbers
        numbers.put("RW", new String[] {
            "Rimwe", // One
            "Kabiri", // Two
            "Gatatu", // Three
            "Kane", // Four
            "Gatanu", // Five
            "Gatandatu", // Six
            "Karindwi", // Seven
            "Umunani", // Eight
            "Icyenda", // Nine
            "Icumi" // Ten
        });
        
        // Kiswahili numbers
        numbers.put("SW", new String[] {
            "Moja", // One
            "Mbili", // Two
            "Tatu", // Three
            "Nne", // Four
            "Tano", // Five
            "Sita", // Six
            "Saba", // Seven
            "Nane", // Eight
            "Tisa", // Nine
            "Kumi" // Ten
        });
        
        // English numbers
        numbers.put("EN", new String[] {
            "One",
            "Two",
            "Three",
            "Four",
            "Five",
            "Six",
            "Seven",
            "Eight",
            "Nine",
            "Ten"
        });
        
        return numbers;
    }
    
    /**
     * Get family relationship terms in different languages.
     * @return Map of language code to family relationship terms
     */
    public static Map<String, Map<String, String>> getFamilyTerms() {
        Map<String, Map<String, String>> familyTerms = new HashMap<>();
        
        // Kinyarwanda family terms
        Map<String, String> rwFamily = new HashMap<>();
        rwFamily.put("mother", "Mama");
        rwFamily.put("father", "Papa");
        rwFamily.put("brother", "Musaza (older), Murumuna (younger)");
        rwFamily.put("sister", "Mushiki (older), Murumuna (younger)");
        rwFamily.put("grandfather", "Sogokuru");
        rwFamily.put("grandmother", "Nyogokuru");
        rwFamily.put("aunt", "Masenge (paternal), Nyirasenge (maternal)");
        rwFamily.put("uncle", "Sekuru (paternal), Nyirarume (maternal)");
        rwFamily.put("son", "Umuhungu");
        rwFamily.put("daughter", "Umukobwa");
        familyTerms.put("RW", rwFamily);
        
        // Kiswahili family terms
        Map<String, String> swFamily = new HashMap<>();
        swFamily.put("mother", "Mama");
        swFamily.put("father", "Baba");
        swFamily.put("brother", "Kaka");
        swFamily.put("sister", "Dada");
        swFamily.put("grandfather", "Babu");
        swFamily.put("grandmother", "Bibi");
        swFamily.put("aunt", "Shangazi (paternal), Khalati (maternal)");
        swFamily.put("uncle", "Mjomba (maternal), Amu (paternal)");
        swFamily.put("son", "Mwana wa kiume");
        swFamily.put("daughter", "Mwana wa kike");
        familyTerms.put("SW", swFamily);
        
        // English family terms
        Map<String, String> enFamily = new HashMap<>();
        enFamily.put("mother", "Mother");
        enFamily.put("father", "Father");
        enFamily.put("brother", "Brother");
        enFamily.put("sister", "Sister");
        enFamily.put("grandfather", "Grandfather");
        enFamily.put("grandmother", "Grandmother");
        enFamily.put("aunt", "Aunt");
        enFamily.put("uncle", "Uncle");
        enFamily.put("son", "Son");
        enFamily.put("daughter", "Daughter");
        familyTerms.put("EN", enFamily);
        
        return familyTerms;
    }
    
    /**
     * Get days of the week in different languages.
     * @return Map of language code to days of the week
     */
    public static Map<String, String[]> getDaysOfWeek() {
        Map<String, String[]> days = new HashMap<>();
        
        // Kinyarwanda days
        days.put("RW", new String[] {
            "Kuwa mbere", // Monday
            "Kuwa kabiri", // Tuesday
            "Kuwa gatatu", // Wednesday
            "Kuwa kane", // Thursday
            "Kuwa gatanu", // Friday
            "Kuwa gatandatu", // Saturday
            "Ku cyumweru" // Sunday
        });
        
        // Kiswahili days
        days.put("SW", new String[] {
            "Jumatatu", // Monday
            "Jumanne", // Tuesday
            "Jumatano", // Wednesday
            "Alhamisi", // Thursday
            "Ijumaa", // Friday
            "Jumamosi", // Saturday
            "Jumapili" // Sunday
        });
        
        // English days
        days.put("EN", new String[] {
            "Monday",
            "Tuesday",
            "Wednesday",
            "Thursday",
            "Friday",
            "Saturday",
            "Sunday"
        });
        
        return days;
    }
    
    /**
     * Get months of the year in different languages.
     * @return Map of language code to months of the year
     */
    public static Map<String, String[]> getMonthsOfYear() {
        Map<String, String[]> months = new HashMap<>();
        
        // Kinyarwanda months (modern usage often uses adaptations of English names)
        months.put("RW", new String[] {
            "Mutarama", // January
            "Gashyantare", // February
            "Werurwe", // March
            "Mata", // April
            "Gicurasi", // May
            "Kamena", // June
            "Nyakanga", // July
            "Kanama", // August
            "Nzeli", // September
            "Ukwakira", // October
            "Ugushyingo", // November
            "Ukuboza" // December
        });
        
        // Kiswahili months
        months.put("SW", new String[] {
            "Januari", // January
            "Februari", // February
            "Machi", // March
            "Aprili", // April
            "Mei", // May
            "Juni", // June
            "Julai", // July
            "Agosti", // August
            "Septemba", // September
            "Oktoba", // October
            "Novemba", // November
            "Desemba" // December
        });
        
        // English months
        months.put("EN", new String[] {
            "January",
            "February",
            "March",
            "April",
            "May",
            "June",
            "July",
            "August",
            "September",
            "October",
            "November",
            "December"
        });
        
        return months;
    }
    
    /**
     * Get common phrases in different languages.
     * @return Map of language code to common phrases
     */
    public static Map<String, Map<String, String>> getCommonPhrases() {
        Map<String, Map<String, String>> phrases = new HashMap<>();
        
        // Kinyarwanda phrases
        Map<String, String> rwPhrases = new HashMap<>();
        rwPhrases.put("I don't understand", "Sinumva");
        rwPhrases.put("Please speak slowly", "Vuga buhoro");
        rwPhrases.put("What is your name?", "Witwa nde?");
        rwPhrases.put("My name is...", "Nitwa...");
        rwPhrases.put("Where is the bathroom?", "Aho binyirwa hari he?");
        rwPhrases.put("How much does this cost?", "Birahenda angahe?");
        rwPhrases.put("I am lost", "Nazimiye");
        rwPhrases.put("Can you help me?", "Wambwira?");
        rwPhrases.put("I am hungry", "Mfite inzara");
        rwPhrases.put("I am thirsty", "Mfite inyota");
        phrases.put("RW", rwPhrases);
        
        // Kiswahili phrases
        Map<String, String> swPhrases = new HashMap<>();
        swPhrases.put("I don't understand", "Sielewi");
        swPhrases.put("Please speak slowly", "Tafadhali sema polepole");
        swPhrases.put("What is your name?", "Jina lako ni nani?");
        swPhrases.put("My name is...", "Jina langu ni...");
        swPhrases.put("Where is the bathroom?", "Choo kiko wapi?");
        swPhrases.put("How much does this cost?", "Hii ni bei gani?");
        swPhrases.put("I am lost", "Nimepotea");
        swPhrases.put("Can you help me?", "Unaweza kunisaidia?");
        swPhrases.put("I am hungry", "Nina njaa");
        swPhrases.put("I am thirsty", "Nina kiu");
        phrases.put("SW", swPhrases);
        
        // English phrases
        Map<String, String> enPhrases = new HashMap<>();
        enPhrases.put("I don't understand", "I don't understand");
        enPhrases.put("Please speak slowly", "Please speak slowly");
        enPhrases.put("What is your name?", "What is your name?");
        enPhrases.put("My name is...", "My name is...");
        enPhrases.put("Where is the bathroom?", "Where is the bathroom?");
        enPhrases.put("How much does this cost?", "How much does this cost?");
        enPhrases.put("I am lost", "I am lost");
        enPhrases.put("Can you help me?", "Can you help me?");
        enPhrases.put("I am hungry", "I am hungry");
        enPhrases.put("I am thirsty", "I am thirsty");
        phrases.put("EN", enPhrases);
        
        return phrases;
    }
    
    /**
     * Get color terms in different languages.
     * @return Map of language code to color terms
     */
    public static Map<String, Map<String, String>> getColors() {
        Map<String, Map<String, String>> colors = new HashMap<>();
        
        // Kinyarwanda colors
        Map<String, String> rwColors = new HashMap<>();
        rwColors.put("red", "Umutuku");
        rwColors.put("blue", "Ubururu");
        rwColors.put("green", "Icyatsi");
        rwColors.put("yellow", "Umuhondo");
        rwColors.put("black", "Umukara");
        rwColors.put("white", "Umweru");
        rwColors.put("brown", "Ikigina");
        rwColors.put("orange", "Oranje");
        rwColors.put("purple", "Purupure");
        rwColors.put("pink", "Pinki");
        colors.put("RW", rwColors);
        
        // Kiswahili colors
        Map<String, String> swColors = new HashMap<>();
        swColors.put("red", "Nyekundu");
        swColors.put("blue", "Bluu");
        swColors.put("green", "Kijani");
        swColors.put("yellow", "Njano");
        swColors.put("black", "Nyeusi");
        swColors.put("white", "Nyeupe");
        swColors.put("brown", "Kahawia");
        swColors.put("orange", "Rangi ya machungwa");
        swColors.put("purple", "Zambarau");
        swColors.put("pink", "Waridi");
        colors.put("SW", swColors);
        
        // English colors
        Map<String, String> enColors = new HashMap<>();
        enColors.put("red", "Red");
        enColors.put("blue", "Blue");
        enColors.put("green", "Green");
        enColors.put("yellow", "Yellow");
        enColors.put("black", "Black");
        enColors.put("white", "White");
        enColors.put("brown", "Brown");
        enColors.put("orange", "Orange");
        enColors.put("purple", "Purple");
        enColors.put("pink", "Pink");
        colors.put("EN", enColors);
        
        return colors;
    }
    
    /**
     * Get food and drink terms in different languages.
     * @return Map of language code to food and drink terms
     */
    public static Map<String, Map<String, String>> getFoodAndDrinks() {
        Map<String, Map<String, String>> foodAndDrinks = new HashMap<>();
        
        // Kinyarwanda food and drinks
        Map<String, String> rwFoodAndDrinks = new HashMap<>();
        rwFoodAndDrinks.put("water", "Amazi");
        rwFoodAndDrinks.put("milk", "Amata");
        rwFoodAndDrinks.put("tea", "Icyayi");
        rwFoodAndDrinks.put("coffee", "Ikawa");
        rwFoodAndDrinks.put("juice", "Umutobe");
        rwFoodAndDrinks.put("beer", "Inzoga");
        rwFoodAndDrinks.put("bread", "Umugati");
        rwFoodAndDrinks.put("rice", "Umuceri");
        rwFoodAndDrinks.put("beans", "Ibishyimbo");
        rwFoodAndDrinks.put("meat", "Inyama");
        rwFoodAndDrinks.put("fish", "Ifi");
        rwFoodAndDrinks.put("chicken", "Inkoko");
        rwFoodAndDrinks.put("banana", "Umuneke");
        rwFoodAndDrinks.put("potato", "Ikirayi");
        rwFoodAndDrinks.put("vegetables", "Imboga");
        foodAndDrinks.put("RW", rwFoodAndDrinks);
        
        // Kiswahili food and drinks
        Map<String, String> swFoodAndDrinks = new HashMap<>();
        swFoodAndDrinks.put("water", "Maji");
        swFoodAndDrinks.put("milk", "Maziwa");
        swFoodAndDrinks.put("tea", "Chai");
        swFoodAndDrinks.put("coffee", "Kahawa");
        swFoodAndDrinks.put("juice", "Juisi");
        swFoodAndDrinks.put("beer", "Bia");
        swFoodAndDrinks.put("bread", "Mkate");
        swFoodAndDrinks.put("rice", "Wali");
        swFoodAndDrinks.put("beans", "Maharagwe");
        swFoodAndDrinks.put("meat", "Nyama");
        swFoodAndDrinks.put("fish", "Samaki");
        swFoodAndDrinks.put("chicken", "Kuku");
        swFoodAndDrinks.put("banana", "Ndizi");
        swFoodAndDrinks.put("potato", "Kiazi");
        swFoodAndDrinks.put("vegetables", "Mboga");
        foodAndDrinks.put("SW", swFoodAndDrinks);
        
        // English food and drinks
        Map<String, String> enFoodAndDrinks = new HashMap<>();
        enFoodAndDrinks.put("water", "Water");
        enFoodAndDrinks.put("milk", "Milk");
        enFoodAndDrinks.put("tea", "Tea");
        enFoodAndDrinks.put("coffee", "Coffee");
        enFoodAndDrinks.put("juice", "Juice");
        enFoodAndDrinks.put("beer", "Beer");
        enFoodAndDrinks.put("bread", "Bread");
        enFoodAndDrinks.put("rice", "Rice");
        enFoodAndDrinks.put("beans", "Beans");
        enFoodAndDrinks.put("meat", "Meat");
        enFoodAndDrinks.put("fish", "Fish");
        enFoodAndDrinks.put("chicken", "Chicken");
        enFoodAndDrinks.put("banana", "Banana");
        enFoodAndDrinks.put("potato", "Potato");
        enFoodAndDrinks.put("vegetables", "Vegetables");
        foodAndDrinks.put("EN", enFoodAndDrinks);
        
        return foodAndDrinks;
    }
    
    /**
     * Get weather terms in different languages.
     * @return Map of language code to weather terms
     */
    public static Map<String, Map<String, String>> getWeatherTerms() {
        Map<String, Map<String, String>> weatherTerms = new HashMap<>();
        
        // Kinyarwanda weather terms
        Map<String, String> rwWeather = new HashMap<>();
        rwWeather.put("sunny", "Hanze harasutamye");
        rwWeather.put("cloudy", "Harakwibye");
        rwWeather.put("rainy", "Imvura iragwa");
        rwWeather.put("windy", "Umuyaga urahuha");
        rwWeather.put("cold", "Harakonje");
        rwWeather.put("hot", "Harashyushye");
        rwWeather.put("snow", "Urubura");
        rwWeather.put("fog", "Igihu");
        rwWeather.put("storm", "Inkubi y'umuyaga");
        rwWeather.put("thunder", "Inkuba");
        rwWeather.put("lightning", "Umurabyo");
        weatherTerms.put("RW", rwWeather);
        
        // Kiswahili weather terms
        Map<String, String> swWeather = new HashMap<>();
        swWeather.put("sunny", "Kuna jua");
        swWeather.put("cloudy", "Kuna mawingu");
        swWeather.put("rainy", "Kuna mvua");
        swWeather.put("windy", "Kuna upepo");
        swWeather.put("cold", "Kuna baridi");
        swWeather.put("hot", "Kuna joto");
        swWeather.put("snow", "Theluji");
        swWeather.put("fog", "Ukungu");
        swWeather.put("storm", "Dhoruba");
        swWeather.put("thunder", "Radi");
        swWeather.put("lightning", "Umeme");
        weatherTerms.put("SW", swWeather);
        
        // English weather terms
        Map<String, String> enWeather = new HashMap<>();
        enWeather.put("sunny", "Sunny");
        enWeather.put("cloudy", "Cloudy");
        enWeather.put("rainy", "Rainy");
        enWeather.put("windy", "Windy");
        enWeather.put("cold", "Cold");
        enWeather.put("hot", "Hot");
        enWeather.put("snow", "Snow");
        enWeather.put("fog", "Fog");
        enWeather.put("storm", "Storm");
        enWeather.put("thunder", "Thunder");
        enWeather.put("lightning", "Lightning");
        weatherTerms.put("EN", enWeather);
        
        return weatherTerms;
    }
    
    /**
     * Get common verbs in different languages.
     * @return Map of language code to common verbs
     */
    public static Map<String, Map<String, String>> getCommonVerbs() {
        Map<String, Map<String, String>> verbs = new HashMap<>();
        
        // Kinyarwanda verbs
        Map<String, String> rwVerbs = new HashMap<>();
        rwVerbs.put("to be", "Kuba");
        rwVerbs.put("to have", "Kugira");
        rwVerbs.put("to go", "Kujya");
        rwVerbs.put("to come", "Kuza");
        rwVerbs.put("to eat", "Kurya");
        rwVerbs.put("to drink", "Kunywa");
        rwVerbs.put("to sleep", "Kuryama");
        rwVerbs.put("to speak", "Kuvuga");
        rwVerbs.put("to listen", "Kumva");
        rwVerbs.put("to read", "Gusoma");
        rwVerbs.put("to write", "Kwandika");
        rwVerbs.put("to see", "Kubona");
        rwVerbs.put("to know", "Kumenya");
        rwVerbs.put("to work", "Gukora");
        rwVerbs.put("to live", "Kubaho");
        verbs.put("RW", rwVerbs);
        
        // Kiswahili verbs
        Map<String, String> swVerbs = new HashMap<>();
        swVerbs.put("to be", "Kuwa");
        swVerbs.put("to have", "Kuwa na");
        swVerbs.put("to go", "Kwenda");
        swVerbs.put("to come", "Kuja");
        swVerbs.put("to eat", "Kula");
        swVerbs.put("to drink", "Kunywa");
        swVerbs.put("to sleep", "Kulala");
        swVerbs.put("to speak", "Kusema");
        swVerbs.put("to listen", "Kusikiliza");
        swVerbs.put("to read", "Kusoma");
        swVerbs.put("to write", "Kuandika");
        swVerbs.put("to see", "Kuona");
        swVerbs.put("to know", "Kujua");
        swVerbs.put("to work", "Kufanya kazi");
        swVerbs.put("to live", "Kuishi");
        verbs.put("SW", swVerbs);
        
        // English verbs
        Map<String, String> enVerbs = new HashMap<>();
        enVerbs.put("to be", "To be");
        enVerbs.put("to have", "To have");
        enVerbs.put("to go", "To go");
        enVerbs.put("to come", "To come");
        enVerbs.put("to eat", "To eat");
        enVerbs.put("to drink", "To drink");
        enVerbs.put("to sleep", "To sleep");
        enVerbs.put("to speak", "To speak");
        enVerbs.put("to listen", "To listen");
        enVerbs.put("to read", "To read");
        enVerbs.put("to write", "To write");
        enVerbs.put("to see", "To see");
        enVerbs.put("to know", "To know");
        enVerbs.put("to work", "To work");
        enVerbs.put("to live", "To live");
        verbs.put("EN", enVerbs);
        
        return verbs;
    }
    
    /**
     * Get past tense examples in different languages.
     * @return Map of language code to past tense examples
     */
    public static Map<String, Map<String, String>> getPastTenseExamples() {
        Map<String, Map<String, String>> pastTense = new HashMap<>();
        
        // Kinyarwanda past tense examples
        Map<String, String> rwPastTense = new HashMap<>();
        rwPastTense.put("I went", "Nagiye");
        rwPastTense.put("I ate", "Ndiye");
        rwPastTense.put("I spoke", "Navuze");
        rwPastTense.put("I saw", "Nabonye");
        rwPastTense.put("I learned", "Nize");
        rwPastTense.put("I worked", "Nakoze");
        rwPastTense.put("I came", "Naje");
        rwPastTense.put("I read", "Nasomye");
        rwPastTense.put("I wrote", "Nanditse");
        rwPastTense.put("I lived", "Nabayeho");
        pastTense.put("RW", rwPastTense);
        
        // Kiswahili past tense examples
        Map<String, String> swPastTense = new HashMap<>();
        swPastTense.put("I went", "Nilienda");
        swPastTense.put("I ate", "Nilikula");
        swPastTense.put("I spoke", "Nilisema");
        swPastTense.put("I saw", "Niliona");
        swPastTense.put("I learned", "Nilijifunza");
        swPastTense.put("I worked", "Nilifanya kazi");
        swPastTense.put("I came", "Nilikuja");
        swPastTense.put("I read", "Nilisoma");
        swPastTense.put("I wrote", "Niliandika");
        swPastTense.put("I lived", "Niliishi");
        pastTense.put("SW", swPastTense);
        
        // English past tense examples
        Map<String, String> enPastTense = new HashMap<>();
        enPastTense.put("I went", "I went");
        enPastTense.put("I ate", "I ate");
        enPastTense.put("I spoke", "I spoke");
        enPastTense.put("I saw", "I saw");
        enPastTense.put("I learned", "I learned");
        enPastTense.put("I worked", "I worked");
        enPastTense.put("I came", "I came");
        enPastTense.put("I read", "I read");
        enPastTense.put("I wrote", "I wrote");
        enPastTense.put("I lived", "I lived");
        pastTense.put("EN", enPastTense);
        
        return pastTense;
    }
    
    /**
     * Get future tense examples in different languages.
     * @return Map of language code to future tense examples
     */
    public static Map<String, Map<String, String>> getFutureTenseExamples() {
        Map<String, Map<String, String>> futureTense = new HashMap<>();
        
        // Kinyarwanda future tense examples
        Map<String, String> rwFutureTense = new HashMap<>();
        rwFutureTense.put("I will go", "Nzajya");
        rwFutureTense.put("I will eat", "Ndaza kurya");
        rwFutureTense.put("I will speak", "Nzavuga");
        rwFutureTense.put("I will see", "Nzabona");
        rwFutureTense.put("I will learn", "Nziga");
        rwFutureTense.put("I will work", "Nzakora");
        rwFutureTense.put("I will come", "Nzaza");
        rwFutureTense.put("I will read", "Nzasoma");
        rwFutureTense.put("I will write", "Nzandika");
        rwFutureTense.put("I will live", "Nzabaho");
        futureTense.put("RW", rwFutureTense);
        
        // Kiswahili future tense examples
        Map<String, String> swFutureTense = new HashMap<>();
        swFutureTense.put("I will go", "Nitaenda");
        swFutureTense.put("I will eat", "Nitakula");
        swFutureTense.put("I will speak", "Nitasema");
        swFutureTense.put("I will see", "Nitaona");
        swFutureTense.put("I will learn", "Nitajifunza");
        swFutureTense.put("I will work", "Nitafanya kazi");
        swFutureTense.put("I will come", "Nitakuja");
        swFutureTense.put("I will read", "Nitasoma");
        swFutureTense.put("I will write", "Nitaandika");
        swFutureTense.put("I will live", "Nitaishi");
        futureTense.put("SW", swFutureTense);
        
        // English future tense examples
        Map<String, String> enFutureTense = new HashMap<>();
        enFutureTense.put("I will go", "I will go");
        enFutureTense.put("I will eat", "I will eat");
        enFutureTense.put("I will speak", "I will speak");
        enFutureTense.put("I will see", "I will see");
        enFutureTense.put("I will learn", "I will learn");
        enFutureTense.put("I will work", "I will work");
        enFutureTense.put("I will come", "I will come");
        enFutureTense.put("I will read", "I will read");
        enFutureTense.put("I will write", "I will write");
        enFutureTense.put("I will live", "I will live");
        futureTense.put("EN", enFutureTense);
        
        return futureTense;
    }
    
    /**
     * Get direction/navigation phrases in different languages.
     * @return Map of language code to direction/navigation phrases
     */
    public static Map<String, Map<String, String>> getDirectionPhrases() {
        Map<String, Map<String, String>> directions = new HashMap<>();
        
        // Kinyarwanda direction phrases
        Map<String, String> rwDirections = new HashMap<>();
        rwDirections.put("left", "Ibumoso");
        rwDirections.put("right", "Iburyo");
        rwDirections.put("straight ahead", "Gukomeza imbere");
        rwDirections.put("turn around", "Kugaruka inyuma");
        rwDirections.put("north", "Amajyaruguru");
        rwDirections.put("south", "Amajyepfo");
        rwDirections.put("east", "Iburasirazuba");
        rwDirections.put("west", "Iburengerazuba");
        rwDirections.put("near", "Hafi");
        rwDirections.put("far", "Kure");
        rwDirections.put("Where is...?", "...iri he?");
        rwDirections.put("How far is...?", "...iri kure kangahe?");
        directions.put("RW", rwDirections);
        
        // Kiswahili direction phrases
        Map<String, String> swDirections = new HashMap<>();
        swDirections.put("left", "Kushoto");
        swDirections.put("right", "Kulia");
        swDirections.put("straight ahead", "Moja kwa moja");
        swDirections.put("turn around", "Geuka");
        swDirections.put("north", "Kaskazini");
        swDirections.put("south", "Kusini");
        swDirections.put("east", "Mashariki");
        swDirections.put("west", "Magharibi");
        swDirections.put("near", "Karibu");
        swDirections.put("far", "Mbali");
        swDirections.put("Where is...?", "...iko wapi?");
        swDirections.put("How far is...?", "...iko mbali kiasi gani?");
        directions.put("SW", swDirections);
        
        // English direction phrases
        Map<String, String> enDirections = new HashMap<>();
        enDirections.put("left", "Left");
        enDirections.put("right", "Right");
        enDirections.put("straight ahead", "Straight ahead");
        enDirections.put("turn around", "Turn around");
        enDirections.put("north", "North");
        enDirections.put("south", "South");
        enDirections.put("east", "East");
        enDirections.put("west", "West");
        enDirections.put("near", "Near");
        enDirections.put("far", "Far");
        enDirections.put("Where is...?", "Where is...?");
        enDirections.put("How far is...?", "How far is...?");
        directions.put("EN", enDirections);
        
        return directions;
    }
    
    /**
     * Get common idioms and proverbs in different languages.
     * @return Map of language code to idioms and proverbs
     */
    public static Map<String, Map<String, String>> getIdiomsAndProverbs() {
        Map<String, Map<String, String>> idioms = new HashMap<>();
        
        // Kinyarwanda idioms and proverbs
        Map<String, String> rwIdioms = new HashMap<>();
        rwIdioms.put("Akari imuhana karuta ak'iwanyu", "A neighbor's help is better than that from a distant relative");
        rwIdioms.put("Inyana ni iya mweru", "The calf belongs to its mother (your children will always be yours)");
        rwIdioms.put("Ibintu by'abandi biraryoha", "Other people's things are always sweeter");
        rwIdioms.put("Umugabo umwe agerwa kuri rimwe", "A single man is struck only once (unity is strength)");
        rwIdioms.put("Ukize ubukene arakibuka", "One who has overcome poverty remembers it");
        idioms.put("RW", rwIdioms);
        
        // Kiswahili idioms and proverbs
        Map<String, String> swIdioms = new HashMap<>();
        swIdioms.put("Haraka haraka haina baraka", "Hurry hurry has no blessing (haste makes waste)");
        swIdioms.put("Asiyesikia la mkuu huvunjika guu", "One who doesn't listen to an elder breaks their leg");
        swIdioms.put("Mtoto wa nyoka ni nyoka", "The child of a snake is a snake");
        swIdioms.put("Pole pole ndio mwendo", "Slowly slowly is the way to go");
        swIdioms.put("Mgeni siku mbili; siku ya tatu mpe jembe", "A guest stays for two days; on the third day give them a hoe");
        idioms.put("SW", swIdioms);
        
        // English idioms and proverbs
        Map<String, String> enIdioms = new HashMap<>();
        enIdioms.put("A bird in the hand is worth two in the bush", "What you have is more valuable than what you might get");
        enIdioms.put("Don't count your chickens before they hatch", "Don't make plans that depend on something good happening before it's happened");
        enIdioms.put("Actions speak louder than words", "What someone does means more than what they say");
        enIdioms.put("The early bird catches the worm", "People who wake up and start work early have the best chance of success");
        enIdioms.put("You can't teach an old dog new tricks", "It's difficult to teach someone new skills or to change someone's habits or character");
        idioms.put("EN", enIdioms);
        
        return idioms;
    }
    
    /**
     * Generate a sample cultural note in the specified language.
     * @param languageCode The language code
     * @return A cultural note
     */
    public static String getCulturalNote(String languageCode) {
        switch (languageCode) {
            case "RW":
                return "In Rwanda, greetings are very important in social interactions. When greeting someone older or in a position of authority, it's customary to offer your right hand while touching your right forearm with your left hand as a sign of respect. The traditional Rwandan dress is called 'Umushanana' for women, consisting of a wrapped skirt and a sash over one shoulder. A significant cultural tradition is 'Umuganda', a national day of community service held on the last Saturday of each month, where citizens work together on public projects.";
            case "SW":
                return "In Swahili-speaking regions, particularly along the East African coast, the greeting 'Jambo' is often used with tourists, while locals typically greet each other with 'Habari' (How are you?). Swahili culture has been influenced by Arab, Persian, Indian, and European contacts over centuries. In Swahili cuisine, spices like cardamom, cinnamon, and cloves are commonly used, reflecting these cultural influences. 'Harambee' is an important concept, representing community self-help events where people gather resources to help someone in need.";
            case "EN":
                return "English is spoken as an official language in several African countries, including Nigeria, Ghana, Kenya, and South Africa. In these contexts, English has developed unique characteristics influenced by local languages and cultures. In many African countries, English serves as a unifying language among people who speak different tribal or regional languages. The form of English spoken often incorporates local expressions, grammatical variations, and pronunciations that differ from British or American English.";
            default:
                return "Cultural information not available for this language.";
        }
    }
}