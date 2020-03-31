package com.townfeednews.fragments;


//import com.townfeednews.IOInterface.SpeakText;
//import com.townfeednews.adapter.VerticalPagerAdapter;

//
//
///**
// * A simple {@link Fragment} subclass.
// */
//public class NewsReaderFragment extends Fragment {
//
//    private static final String TAG = "NewsReaderFragment";
////    private VerticalPagerAdapter verticalViewPagerAdapter;
//    private TextToSpeech textToSpeech;

//    SpeakText speakText = new SpeakText() {
//        @Override
//        public void startSpeakText(int position) {
//            setTextSpeaker(position);
//        }
//
//        @Override
//        public void stopSpeakText() {
//            textToSpeech.stop();
//        }
//
//
//    };

//    private void setTextSpeaker(int position) {
//        HashMap<String, String> params = new HashMap<String, String>();
//
//        params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "stringId");
//        String text = newsArrayList.get(position).getTitle() + " \n\n\n\n " + newsArrayList.get(position).getDetails();
////        String text = "केंद्र सरकार ने दस लाख आशा कर्मियों का मानदेय दोगुना किया। आयुष्मान भारत योजना के तहत सरकार इस वर्ष चालीस हजार स्वास्थ्य केंद्रों की स्थापना करेगी।";
////        textToSpeech.setSpeechRate(0.8f);
////        textToSpeech.setPitch(0.8f);
//        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, params);
//    }

//    public NewsReaderFragment() {
//        // Required empty public constructor
//    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_news_reader, container, false);
//        Log.d(TAG, "onCreateView: checking...: ");

//        verticalViewPagerAdapter = view.findViewById(R.id.vPager);
//        verticalViewPagerAdapter.setAdapter(new VerticlePagerAdapter(getContext(), newsArrayList, speakText));

//        textToSpeech = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
//            @Override
//            public void onInit(int status) {
//                if (status != TextToSpeech.ERROR) {
//                    textToSpeech.setLanguage(new Locale("en", "IN"));
////                    textToSpeech.setLanguage(new Locale("hi", "IN"));
//                }
//
//                textToSpeech.setOnUtteranceCompletedListener(new TextToSpeech.OnUtteranceCompletedListener() {
//                    @Override
//                    public void onUtteranceCompleted(String utteranceId) {
//                        Log.d(TAG, "onUtteranceCompleted: Current Item : " + verticalViewPagerAdapter.getCurrentItem() + " ArrayList Size : " + newsArrayList.size());
//                        textToSpeech.stop();
//                        verticalViewPagerAdapter.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                if (verticalViewPagerAdapter.getCurrentItem() < newsArrayList.size()) {
//                                    verticalViewPagerAdapter.setCurrentItem(verticalViewPagerAdapter.getCurrentItem() + 1);
//                                }
//                            }
//                        }, 100);

//                    }
//                });
//            }
//
//        });

//        new Thread() {
//            @Override
//            public void run() {
//                if (AppUtils.hasInternetConnection()) {
//                    Log.d("TAG", "onCreateView: Internet Connected.");
//                } else {
//                    Log.d("TAG", "onCreateView: Internet Connection Fail. ");
//                }
//            }
//        }.start();

        // Inflate the layout for this fragment
//        verticalViewPagerAdapter.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                AppPrefs.setUrlForWebView(getContext(), newsArrayList.get(position).getPermalink());
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
//        return view;
//    }
//    @Override
//    public void onDestroy() {
//
//        //Close the Text to Speech Library
//        if(textToSpeech != null) {
//
//            textToSpeech.stop();
//            textToSpeech.shutdown();
//            Log.d(TAG, "TTS Destroyed");
//        }
//        super.onDestroy();
//    }

//}
