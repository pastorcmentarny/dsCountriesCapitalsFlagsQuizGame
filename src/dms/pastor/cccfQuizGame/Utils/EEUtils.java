package dms.pastor.cccfQuizGame.Utils;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import dms.pastor.cccfQuizGame.R;


/**
 * Author Dominik Symonowicz
 * Created: 13/01/2013 23:47
 * WWW:	http://pastor.ovh.org
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: uk.linkedin.com/pub/dominik-symonowicz/5a/706/981/
 * <p>
 * Contains all commons methods for UI manipulation
 */
public final class EEUtils {
    private static final ArrayList<String> WHOOPEES = getMessages();

    private EEUtils() {
    }

    private static ArrayList<String> getMessages() {
        ArrayList<String> messages = new ArrayList<>();
        messages.add("LOL");
        messages.add("Really?");
        messages.add("Woops.");
        messages.add("You are not a Sheldon.");
        messages.add("If you buy me a door, I will shut up.");
        messages.add("SRSLY?");
        messages.add("WTF. I mean .. What The ... fluff");
        messages.add("Lipton is capital of Ceylon");
        messages.add("LOL. It is like saying that A fjord is a Norwegian car");
        messages.add("LOL. It is like saying that The Balkans are an alien people on Star Trek");
        messages.add("What birds are found in Portugal ?  Portu-geese :) ");
        messages.add("Where you learn that ?in Tabloid?");
        messages.add("Don't thank me for insulting you for comment your mistakes, it was a pleasure.");
        messages.add("Errors have been made. Others will be blamed.");
        messages.add("Epic fail");
        messages.add("I hope,you are not Santa,because you will never find where I am.");
        messages.add("Your knowledge about world is as accurate as knowledge about world by North Koreans .");
        messages.add("Some people said that school is like jail...Well... it seems you never been imprisoned.Are you?");
        messages.add("Well.. You doing very well.");
        messages.add("I’m not sure what’s wrong… But it’s probably your fault.");
        messages.add("I'm impressed. You are really ambitious with guess");
        messages.add("Out of my mind. Back in five minutes.");
        messages.add("F A C E P A L M");
        messages.add("Nostradamus predicted that you'd be a loser.");
        return messages;
    }

    public static String getMaybeComment() {
        switch (new Random().nextInt(140)) {
            case 4:
            case 7:
            case 13:
            case 14:
            case 77:
            case 113:
            case 130:
            case 137:
                return WHOOPEES.get(new Random().nextInt(WHOOPEES.size()));
            default:
                return StringUtils.EMPTY_STRING;
        }
    }

    private static ArrayList<String> getTrivia() {
        ArrayList<String> messages = new ArrayList<>();
        messages.add("There are more Albanians outside of Albania than within.(~3 millions live within compare to ~7 millions live abroad)");
        messages.add("Algeria is largest(by area) country in Africa.");
        messages.add("Andorra has never had its own currency.");
        messages.add("All major political parties in Argentina have their own brands of beer.");
        messages.add("Australia was one of the creators of the United Nations. ");
        messages.add("Australia is the only continent in the world without an active volcano. ");
        messages.add("Sting with Algerian singer Cheb Mami sang  'Desert Rose'.One of an amazing pop song in my opinion in 90's");
        messages.add("1 January has become the official birthday for many Afghans who don’t know when they were born. Most of them knows approximate birthday date on the Islamic calendar(they didn't celebrate birthday and  government did NOT have a system  to register births ). Problem occurred after civil war when NATO visited country to help  but it was a problem. gor individuals ,paperwork required a birthday in Roman Calendar, which Afghan people didn't know birthday or how to convert from Roman to Islamic calendar, so they used 1 January as it was easiest to remember.");
        messages.add("Belgium has the highest density of roads and the highest density of railroads in the\n" +
                "world.");
        messages.add("Brazil is leading producers of hydroelectric power.");
        messages.add("Brazil is the largest country in South America.");
        messages.add("Brazil is the highest population in South America.");
        messages.add("Brazil has the 6th highest population in the world.");
        messages.add("Brazil has the 5th largest land area in the world.");
        messages.add("Brazil has won the football world cup 5 times.");
        messages.add("Brazil has longest continuous coastline in the world (7,491 km).");
        messages.add("Brazil is the largest Portuguese-speaking nation in the world.");
        messages.add("Capoeira is a Brazilian martial art.");
        messages.add("Brazil is largest catholic  nation,but there is no official religion in Brazil");
        messages.add("Brazil  is the longest country in the world .It has 4 506 kilometers via land from north to south.");
        messages.add("Football is the most popular sport in Brazil.");
        messages.add("Rio de Janeiro in English means January River.");
        messages.add("Canada is the country with highest number of lakes in the world.");
        messages.add("Canada is largest country  in North America.");
        messages.add("Canada has the longest coastline of any country in the world");
        messages.add("China has the highest population in the world.There is more than 1,343,000,000 people. \n");
        messages.add("China has the highest population of buddhists in the world.\n");
        messages.add("Chinese territory is wide spread and it covers 5 time zone,but whole China using only 1 time zone called Beijing Time  .\n");
        messages.add("The Chinese invented gunpowder.");
        messages.add("The Chinese invented paper.");
        messages.add("France is most popular tourist destination in the world.");
        messages.add("French high-speed train known as TGV holds the record for the fastest wheeled train, reaching 574.8 km/h!");
        messages.add("Louis XIX was one of the shortest ruled King in the world . He was King of France for around 20 minutes.");
        messages.add("Believe it or not there are more people speaking French in Africa than in France.");
        messages.add("Metro (called  known as the Tube and the Underground) is the first underground railway in the world.operation began on 10 January 1863");
        messages.add("England is thought of as having the world’s worst food.(Author of this app strongly agree with that opinion)");
        messages.add("Germany has highest population in Europe (if excluding Russia as it is transcontinental country.) ");
        messages.add("Indonesia is the most populated island country.Population is 242,325,638 (2011).");
        messages.add("Indonesia has the most volcanoes of any country in the world.");
        messages.add("Sumo is Japan's national sport.");
        messages.add("More than 70% of Japan consists of mountains, including more than 200 volcanoes.");
        messages.add("On average there are around 1,500 earthquakes every year in Japan.");
        messages.add("Libya is country covered in 99% by desert.");
        messages.add("Llanfairpwllgwyngyllgogerychwyrndrobwllllantysiliogogogoch is largest a village name in the world.It is on the island of Anglesey in Wales");
        messages.add("Maldives is the smallest country in Asia..");
        messages.add("La paz,(capital of Bolivia) it is highest capital in the world.");
        messages.add("Malta is one of few countries that doesn't have any forest, mountains or rivers.");
        messages.add("Netherlands is official name,but is often called a Holland.");
        messages.add("Netherlands means lowlands.");
        messages.add("Netherlands in one-third is below sea level.");
        messages.add("Nigeria is highest population in Africa.");
        messages.add("Norway(mainland) produces 99% of electricity from renewable energy.");
        messages.add("'Taumatawhakatangihangakoauauotamateahaumaitawhitiurehaeaturipukakapikimaungahoronukupokaiwhenuakitanatahu' is the longest place name in the world(officially).");
        messages.add("Papua New Guinea is a country with over 800(some sources mention even over 850!) spoken languages.");
        messages.add("poland is a country,where author of this program is from.");
        messages.add("One third of poland is covered with forest!");
        messages.add("The Polish born astronomer Nicolaus Copernicus is considered as first person to propose heliocentric model.");
        messages.add("Saudi Arabia has the world's second largest oil reserves but virtually no rivers or lakes (Well,they have handful oasis)");
        messages.add("Seychelles is the smallest country in Africa.");
        messages.add("Shanghai has the highest population in the world.There is more than 17,836,000 people.");
        messages.add("South Africa has 3 capitals Pretoria (executive),Bloemfontein (judicial) , Cape Town (legislative).In my game is Cape Town");
        messages.add("South Africa is a country with most official languages (11!):  Afrikaans, English, Ndebele, Sesotho, Setswana, Sepedi, Swati, Tshivenda, Xhosa, Xitsonga and Zulu");
        messages.add("Sweden is the number one nation in environmental protection and sustainability\n" +
                "policies and aims to be the first country in the world to become oil independent by the\n" +
                "year 2020.");
        messages.add("Sweden has, along with Denmark and Hungary, the highest standard V.A.T. rate in the world (25%)");
        messages.add("Nordstan is a shopping centre in Göteborg (Sweden). It is the largest shopping centre in Scandinavia");
        messages.add("Polish Salt Mine in Wieliczka is one of the oldest salt mine in the world.It is a tourist attraction ");
        messages.add("Poland is a first country in Europe and second in the World to have constitution.");
        messages.add("Sweden has the highest number of nuclear plants per capita, with 10 reactors for 9 million inhabitants.");
        messages.add("Stockholm Globe Arena is currently the largest hemispherical building in the world and took two and a half years to build. Shaped like a large white ball, it has a diameter of 110 metres (361 feet) and an inner height of 85 metres (279 feet).");
        messages.add("Madrid is a highest capital in Europe.");
        messages.add("Madrid is the greenest capital in Europe.");
        messages.add("Spain is famous for Flamenco ,tapas and bullfighting.");
        messages.add("Sweden is famous for ABBA,H&M , IKEA, and Volvo.");
        messages.add("Sweden is the world's third biggest exporter of music (after US,UK");
        messages.add("Sweden is one of the few European Union countries to retain its own currency and not use the euro.");
        messages.add("Sweden is country with highest people per nuclear plant ration (10 nuclear plant for 9.5 mln people ");
        messages.add("Swedes have the longest life expectancy in Europe. (80 to 81 depends depends on source)");
        messages.add("The first ice hotel of the world was built near the village of Jukkasjärvi, Sweden.");
        messages.add("65% of the total land area is covered with forests.");
        messages.add("0. Since 2004, all employers in Sweden have been required to provide free massages for their staff.");
        messages.add("Russia is the country with the most neighbors.(They have a  16 neighbors.)");
        messages.add("Russia is the largest country in the world.");
        messages.add("Thailand has highest proportion of buddhists in the world.");
        messages.add("Ukraine is the largest country in Europe(if exclude Russia,because Russia is on 2 continents. ");
        messages.add("(Ukraine) The Arsenalna station on Kiev Metro's Sviatoshynsko-Brovarska Line is currently the deepest station in the world at 105.5 metres.");
        messages.add("(Ukraine) The world’s heaviest aircraft is An-225 Mriya was created by Antonov design bureau in Kiev.");
        messages.add("Vatican City is the smallest country");
        messages.add("Difference between UK and GB is that Great Britain refers to England ,Scotland and Wales.United Kingdom refers to England, Northern Ireland,Scotland and Wales (and few overseas territories:");
        messages.add("USA is the largest energy-consuming country.");
        messages.add("UK is the world's second biggest exporter of music.");
        messages.add("USA is the world's biggest exporter of music.");
        return messages;
    }

    public static String getRandomTrivia() {
        return "Did you know? " + getTrivia().get(new Random().nextInt(getTrivia().size()));
    }

    public static void displayBlindToast(Context context, Activity activity) {
        View myLayout = setupImageForToast(activity, R.drawable.eye);
        TextView myMessage = myLayout.findViewById(R.id.text_to_display);
        myMessage.setText(R.string.blind);
        displayToast(context, myLayout);
    }

    public static void displayHalfHalfToast(Context context, Activity activity) {
        View myLayout = setupImageForToast(activity, R.drawable.half);
        TextView myMessage = myLayout.findViewById(R.id.text_to_display);
        myMessage.setText(R.string.fifty_fifty_to_guess);
        displayToast(context, myLayout);
    }

    @NonNull
    private static View setupImageForToast(Activity activity, int imageResource) {
        LayoutInflater myInflater = activity.getLayoutInflater();
        View myLayout = myInflater.inflate(R.layout.domtoast, (ViewGroup) activity.findViewById(R.id.toastLayout));
        ImageView myImage = myLayout.findViewById(R.id.img);
        myImage.setImageResource(imageResource);
        return myLayout;
    }

    private static void displayToast(Context context, View myLayout) {
        Toast myToast = new Toast(context.getApplicationContext());
        myToast.setDuration(Toast.LENGTH_LONG);
        myToast.setView(myLayout);
        myToast.show();
    }
}
