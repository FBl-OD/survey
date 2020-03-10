package mg.studio.android.survey.questions;

import android.content.Context;
import android.util.JsonReader;
import android.util.JsonToken;



import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import mg.studio.android.survey.util.GetJson;

/**
 * 读取json文件并且转换成字符串
 * * @param filePath文件的路径
 * * @return
 * * @throws IOException
 */
/*
public class GetJsonData {
    public String getJson(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public JsonReader getJson1(Context context, String fileName) {
        InputStream is = null;
        try {
            is = context.getAssets().open(fileName);
            JsonReader reader = new JsonReader(new InputStreamReader(is));
            reader.beginObject();
            while (reader.hasNext()) {
                reader.beginObject();
                String tag=reader.nextName();
                if(tag.equals("survey")) {
                    reader.beginObject();

                        if (tag.equals("id")) {
                            int id = reader.nextInt();
                            System.out.println("id : " + id);
                        } else if (tag.equals("len")) {
                            Integer age = reader.nextInt();
                            System.out.println("len : " + age);
                        } else if (tag.equals("questions")) {
                            reader.beginArray();
                            int i = 0;
                            while (reader.hasNext()) {
                                System.out.println("car" + (++i) + " : " + reader.nextString());
                            }
                            reader.endArray();
                        }

                }
                Log.i(TAG, "doing: " + doing.toString());
                reader.endObject();
            }
            reader.endArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

*/

/**
 * Created by vince on 2017/7/1.
 */
public class GetJsonData {

    /**
     * 使用JsonReader解析复杂的JSON数据
     */
    public Survey parseJSONSurvey(Context context) {

        String s = new GetJson().getJson("ExampleQuestions.json", context);

/*
        InputStream is = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("ExampleQuestions.json");
        InputStreamReader in = new InputStreamReader(is);
 */
        InputStream is = null;
        try {
            is = new ByteArrayInputStream(s.getBytes("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStreamReader in = new InputStreamReader(is);
        JsonReader jsonReader = new JsonReader(in);
        Survey survey = new Survey();
        try {
            jsonReader.beginObject();
            String name = jsonReader.nextName();
            if ("survey".equals(name)) {
                survey = readSurvey(jsonReader);
            }
            jsonReader.endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(survey.toString());
        return survey;
    }


    //解析一个Survey对象
    private Survey readSurvey(JsonReader jsonReader) {
        Survey m = new Survey();
        try {
            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                String name = jsonReader.nextName();
                if ("id".equals(name)) {
                    m.setId(jsonReader.nextString());
                } else if ("len".equals(name)) {
                    m.setLen(jsonReader.nextInt());
                    //peek 意思是取这个值看一下 返回的是一个标记类型 标记这个值为空 或者为数字 或者为字符串等等类型
                    //因为geo有空的 所以这里用peek判断一下
                } else if ("questions".equals(name) && jsonReader.peek() != JsonToken.NULL) {
                    m.setQuestions(readQuestionArray(jsonReader));
                } else {
                    //处理null的情况 跳过这个值
                    jsonReader.skipValue();
                }
            }
            jsonReader.endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return m;
    }


    /**
     * 解析Question数组
     *
     * @param jsonReader
     * @return
     */
    private ArrayList<Question> readQuestionArray(JsonReader jsonReader) {

        ArrayList<Question> list = new ArrayList<>();
        try {
            //jsonReader.beginObject();
           // String name = jsonReader.nextName();
           // if ("questions".equals(name)) {
                jsonReader.beginArray();
                while (jsonReader.hasNext()) {
                    list.add(readQuestion(jsonReader));
                }
                jsonReader.endArray();
           // }
           // jsonReader.endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }


    /**
     * 解析Question
     *
     * @param jsonReader
     * @return
     */
    private Question readQuestion(JsonReader jsonReader) {
        Question question = new Question();
        try {
            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                String name = jsonReader.nextName();
                if ("type".equals(name)) {
                    question.setType(jsonReader.nextString());
                } else if ("question".equals(name)) {
                    question.setQuestion(jsonReader.nextString());
                } else if ("options".equals(name)) {
                    question.setOption(readoptionArray(jsonReader));
                }
            }
            jsonReader.endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return question;
    }


    /**
     * 解析Option数组
     *
     * @param jsonReader
     * @return
     */
    private ArrayList<Option> readoptionArray(JsonReader jsonReader) {

        ArrayList<Option> list = new ArrayList<>();
        int i = 1;
        try {
            jsonReader.beginArray();
            while (jsonReader.hasNext()) {
                list.add(readOption(jsonReader, String.valueOf(i)));
                i++;
            }
            jsonReader.endArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }


    /**
     * 解析Option
     *
     * @param jsonReader
     * @return
     */
    private Option readOption(JsonReader jsonReader, String s) {
        Option option = new Option();
        try {
            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                String name = jsonReader.nextName();
                if (s.equals(name)) {
                    option.setNumber(jsonReader.nextString());
                }
            }
            jsonReader.endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(option.toString());
        return option;
    }

}


