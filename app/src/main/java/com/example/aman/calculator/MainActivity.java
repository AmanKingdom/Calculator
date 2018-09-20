package com.example.aman.calculator;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public boolean flag;    //used to mark whether an operation is completed.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textview = findViewById(R.id.history);
        textview.setMovementMethod(ScrollingMovementMethod.getInstance());  //you can scroll through the screen when the input is too long.

        flag = true;
    }

    public StringBuffer historyString = new StringBuffer();         //display the previous input
    public StringBuffer currentString = new StringBuffer("0");      //display the current input

    public boolean lenghtLess13() {      //prevent input of more than 12 characters
        if (currentString.length() <= 12) {
            return true;
        } else return false;
    }

    public void showHistory() {      //update the history TextView when the operator is entered
        TextView history = findViewById(R.id.history);
        history.setText(historyString.toString());
    }

    public void showCurrent() {      //time to update
        TextView current = findViewById(R.id.current);
        current.setText(currentString.toString());
    }

    public void clearScreen(View ac) {       //clear the history screen and current screen
        historyString.delete(0, historyString.length());
        currentString.delete(0, currentString.length());
        currentString.append("0");
        showCurrent();
        showHistory();
        flag = true;
    }

    public void delete(View delButton) {     //called by the buttonDel, when the current screen is empty, the history will be deleted next.
        if (currentString.length() == 0 & historyString.length() != 0) {
            currentString.append(historyString.toString());
            historyString.delete(0, historyString.length());
            flag = true;
        }
        if (currentString.length() != 0) {
            currentString.deleteCharAt(currentString.length() - 1);
        }
        if (currentString.length() == 0 & historyString.length() == 0) {
            currentString.append("0");
            flag = true;
        }
        showHistory();
        showCurrent();
    }

    public void numBtnClicked(View numBtn) {     //called by the button which contain numbers
        if (!flag) {
            clearScreen(numBtn);
            flag = true;
        }
        if (lenghtLess13()) {
            Button btn = findViewById(numBtn.getId());
            if (currentString.length() != 0) {
                if (currentString.charAt(currentString.length() - 1) == '/' & btn.getText().toString().equals("0")) {
                    @SuppressLint("ShowToast") Toast toast = Toast.makeText(getApplicationContext(), "除数不可以为0哦", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    if (currentString.toString().equals("0")) {
                        currentString.delete(0, currentString.length());
                    }
                    currentString.append(btn.getText().toString());
                    showCurrent();
                }
            }
            flag = true;
        }
    }

    public void operatorBtnClicked(View operatorBtn) {
        Button btn = findViewById(operatorBtn.getId());
        if (lenghtLess13()) {
            if (!flag) {
                historyString.delete(0, historyString.length());
            }
            if (currentString.length() != 0) {
                if (currentString.charAt(currentString.length() - 1) == '.') {
                    currentString.deleteCharAt(currentString.length() - 1);
                }
            }
            if (!currentString.toString().equals("+") & !currentString.toString().equals("-") & !currentString.toString().equals("*") & !currentString.toString().equals("/")) {
                historyString.append(currentString.toString());
                showHistory();
                currentString.delete(0, currentString.length());
                currentString.append(btn.getText().toString());
            } else {
                currentString.delete(0, currentString.length());
                currentString.append(btn.getText().toString());
            }
            showCurrent();
            flag = true;
        }
    }

    public void pointBtnClicked(View pointBtn) {
        if (!flag) {
            clearScreen(pointBtn);
            flag = true;
        }
        if (lenghtLess13()) {
            if (!currentString.toString().contains(".")) {
                if (currentString.toString().equals("+") | currentString.toString().equals("-") | currentString.toString().equals("*") | currentString.toString().equals("/")) {
                    historyString.append(currentString.toString());
                    showHistory();
                    currentString.delete(0, currentString.length());
                    currentString.append("0.");
                } else {
                    currentString.append(".");
                }
                showCurrent();
            }
        }
    }

    public void calculate(View calcBtn) {
        if (flag) {
            if (!currentString.toString().equals("0")) {
                historyString.append(currentString.toString());
                showHistory();

                double sum = 0;

                String string = historyString.toString();

                if (string.contains("+")) {
                    String[] addArray = string.split("\\+");
                    sum = 0;
                    for (String anAddItem : addArray) {
                        sum = sum + add(anAddItem);
                    }
                } else if (string.contains("-")) {     //有减号无加号
                    String[] subArray = string.split("-");
                    if (string.startsWith("-")) {
                        sum = 0;
                    } else {
                        sum = sub(subArray[0]);
                    }
                    for (int i = 1; i < subArray.length; i++) {
                        sum = sum - sub(subArray[i]);
                    }
                } else if (string.contains("*")) {     //无加减号
                    String[] mulArray = string.split("\\*");
                    sum = 1;
                    for (String anMulItem : mulArray) {
                        sum = sum * mul(anMulItem);
                    }
                } else if (string.contains("/")) {     //只有除号
                    String[] divArray = string.split("/");
                    sum = Double.parseDouble(divArray[0]);
                    for (int i = 1; i < divArray.length; i++) {
                        sum = sum / div(divArray[i]);
                    }
                } else {
                    sum = Double.parseDouble(string);
                }
                currentString.delete(0, currentString.length());
                if (sum % 1 == 0) {
                    int temp = (Double.valueOf(sum)).intValue();
                    currentString.append(String.valueOf(temp));
                } else {
                    currentString.append(String.valueOf(sum));
                }
                showCurrent();
            }
            flag = false;
        }
    }

    public double add(String needAddItem) {
        double addSum = 0;
        if (needAddItem.contains("-")) {
            String[] needSubArray = needAddItem.split("-");
            double subSum;
            if (needAddItem.startsWith("-")) {
                subSum = 0;
            } else {
                subSum = sub(needSubArray[0]);
            }
            for (int i = 1; i < needSubArray.length; i++) {
                subSum = subSum - sub(needSubArray[i]);
            }
            addSum = addSum + subSum;
        } else if (needAddItem.contains("*")) {
            String[] needMulArray = needAddItem.split("\\*");
            double mulSum = 1;
            for (String anMulItem : needMulArray) {
                mulSum = mulSum * mul(anMulItem);
            }
            addSum = addSum + mulSum;
        } else if (needAddItem.contains("/")) {
            String[] needDivArray = needAddItem.split("/");
            double divSum = Double.parseDouble(needDivArray[0]);
            for (int i = 1; i < needDivArray.length; i++) {
                divSum = divSum / div(needDivArray[i]);
            }
            addSum = addSum + divSum;
        } else {
            addSum = Double.parseDouble(needAddItem);
        }
        return addSum;
    }

    public double sub(String needSubItem) {
        double subSum = 0;
        if (needSubItem.contains("*")) {
            String[] needMulArray = needSubItem.split("\\*");
            double mulSum = 1;
            for (String anMulItem : needMulArray) {
                mulSum = mulSum * mul(anMulItem);
            }
            subSum = mulSum;
        } else if (needSubItem.contains("/")) {
            String[] needDivArray = needSubItem.split("/");
            double divSum = Double.parseDouble(needDivArray[0]);
            for (int i = 1; i < needDivArray.length; i++) {
                divSum = divSum / div(needDivArray[i]);
            }
            subSum = divSum;
        } else {
            subSum = Double.parseDouble(needSubItem);
        }
        return subSum;
    }

    public double mul(String needMulItem) {
        double mulSum = 1;
        if (needMulItem.contains("/")) {
            String[] needDivArray = needMulItem.split("/");
            double divSum = Double.parseDouble(needDivArray[0]);
            for (int i = 1; i < needDivArray.length; i++) {
                divSum = divSum / div(needDivArray[i]);
            }
            mulSum = mulSum * divSum;
        } else {
            mulSum = mulSum * Double.parseDouble(needMulItem);
        }
        return mulSum;
    }

    public double div(String needDivItem) {
        double divSum = Double.parseDouble(needDivItem);
        return divSum;
    }

    public void shutdown(View offBtn) {
        System.exit(0);
    }
}
