
package CALC;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;
import javafx.scene.effect.DropShadow;

import java.math.BigDecimal;
public class Calculator extends Application {

    // ألوان التصميم
    private final String TITANIUM = "#D5D4CD";
    private final String GUAVA = "#C96C80";
    private final String KOBI = "#D197A2";
    private final String BALLERINA = "#E6C0C9";
    private final String MATCHA = "#BCCA7D";
    private final String TEXT_COLOR = "#67434D";

    private Label display;
    private Label expression;

    private double firstNumber = 0;
    private String operator = "";
    private boolean startNewNumber = true;

    @Override
    public void start(Stage stage) {

        // العنوان
        Label title = new Label("♡ CALCULATOR ♡");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 23));
        title.setTextFill(Color.web(GUAVA));

        Label subtitle = new Label("");
        subtitle.setFont(Font.font("Arial", 12));
        subtitle.setTextFill(Color.web("#98747B"));

        // شاشة العمليات
        expression = new Label("");
        expression.setFont(Font.font("Arial", 13));
        expression.setTextFill(Color.web("#98747B"));
        expression.setMaxWidth(Double.MAX_VALUE);
        expression.setAlignment(Pos.CENTER_RIGHT);

        // شاشة النتيجة
        display = new Label("0");
        display.setFont(Font.font("Arial", FontWeight.BOLD, 38));
        display.setTextFill(Color.web(TEXT_COLOR));
        display.setAlignment(Pos.CENTER_RIGHT);
        display.setMaxWidth(Double.MAX_VALUE);
        display.setMinHeight(65);

        VBox screen = new VBox(12, expression, display);
        screen.setAlignment(Pos.CENTER_RIGHT);
        screen.setPadding(new Insets(20));

        screen.setStyle(
            "-fx-background-color: " + BALLERINA + ";" +
            "-fx-background-radius: 25;"
        );

        screen.setEffect(new DropShadow(
            12, Color.rgb(130, 80, 90, 0.15)
        ));

        // الأزرار
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(11);
        grid.setVgap(11);

        String[][] buttons = {
            {"AC", "⌫", "%", "÷"},
            {"7", "8", "9", "×"},
            {"4", "5", "6", "-"},
            {"1", "2", "3", "+"},
            {"±", "0", ".", "="}
        };

        for (int row = 0; row < buttons.length; row++) {

            for (int col = 0; col < buttons[row].length; col++) {

                String value = buttons[row][col];

                Button btn = new Button(value);

                btn.setPrefSize(76, 66);
                btn.setMinSize(76, 66);
                btn.setFont(Font.font(
                    "Arial", FontWeight.BOLD, 20
                ));

                btn.setTextFill(Color.web(TEXT_COLOR));
                btn.setStyle(getButtonStyle(value, false));

                // ظل ناعم للأزرار
                btn.setEffect(new DropShadow(
                    5, Color.rgb(100, 60, 70, 0.12)
                ));

                // تأثير عند مرور الماوس
                btn.setOnMouseEntered(e -> {
                    btn.setScaleX(1.06);
                    btn.setScaleY(1.06);
                    btn.setStyle(getButtonStyle(value, true));
                });

                btn.setOnMouseExited(e -> {
                    btn.setScaleX(1);
                    btn.setScaleY(1);
                    btn.setStyle(getButtonStyle(value, false));
                });

                // حركة عند الضغط
                btn.setOnAction(e -> {

                    ScaleTransition animation =
                        new ScaleTransition(
                            Duration.millis(100), btn
                        );

                    animation.setFromX(0.93);
                    animation.setFromY(0.93);
                    animation.setToX(1);
                    animation.setToY(1);
                    animation.play();

                    handleButton(value);
                });

                grid.add(btn, col, row);
            }
        }

        // عبارة أسفل الآلة
        Label footer = new Label("୨୧");
        footer.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        footer.setTextFill(Color.web(GUAVA));

        // التصميم الرئيسي
        VBox root = new VBox(
            12,
            title,
            subtitle,
            screen,
            grid,
            footer
        );

        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(25));

        root.setStyle(
            "-fx-background-color: " + TITANIUM + ";"
        );

        // النافذة
        Scene scene = new Scene(root, 410, 650);

        stage.setTitle("My Little Calculator ♡");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    // تصميم الأزرار وألوانها
    private String getButtonStyle(String value, boolean hover) {

        String color = KOBI;

        if (value.equals("=")) {
            color = MATCHA;
        }
        else if ("÷×-+".contains(value)) {
            color = GUAVA;
        }
        else if ("AC⌫%±".contains(value)) {
            color = BALLERINA;
        }

        if (hover) {
            color = hoverColor(value);
        }

        return
            "-fx-background-color: " + color + ";" +
            "-fx-background-radius: 20;" +
            "-fx-border-radius: 20;" +
            "-fx-border-color: rgba(255,255,255,0.45);" +
            "-fx-border-width: 1.5;" +
            "-fx-cursor: hand;" +
            "-fx-font-weight: bold;";
    }

    private String hoverColor(String value) {

        if (value.equals("=")) {
            return "#A8B86D";
        }

        if ("÷×-+".contains(value)) {
            return "#B95770";
        }

        if ("AC⌫%±".contains(value)) {
            return "#DCAAB6";
        }

        return "#DDB1BC";
    }

    // التعامل مع ضغط الأزرار
    private void handleButton(String value) {

        try {

            // إدخال الأرقام
            if (value.matches("[0-9]")) {

                if (startNewNumber ||
                    display.getText().equals("Error")) {

                    display.setText(value);
                    startNewNumber = false;

                } else if (display.getText().equals("0")) {

                    display.setText(value);

                } else {

                    display.setText(
                        display.getText() + value
                    );
                }
            }

            // النقطة العشرية
            else if (value.equals(".")) {

                if (startNewNumber) {

                    display.setText("0.");
                    startNewNumber = false;

                } else if (!display.getText().contains(".")) {

                    display.setText(
                        display.getText() + "."
                    );
                }
            }

            // مسح الكل
            else if (value.equals("AC")) {

                display.setText("0");
                expression.setText("YOUR CALCULATION ♡");

                firstNumber = 0;
                operator = "";
                startNewNumber = true;
            }

            // حذف آخر رقم
            else if (value.equals("⌫")) {

                if (!startNewNumber &&
                    !display.getText().equals("Error")) {

                    String text = display.getText();

                    if (text.length() > 1) {

                        display.setText(
                            text.substring(0, text.length() - 1)
                        );

                    } else {

                        display.setText("0");
                    }
                }
            }

            // تغيير الإشارة
            else if (value.equals("±")) {

                double number = getCurrentNumber();

                display.setText(format(-number));
            }

            // النسبة المئوية
            else if (value.equals("%")) {

                double number = getCurrentNumber();

                display.setText(format(number / 100));
            }

            // العمليات الحسابية
            else if ("+-×÷".contains(value)) {

                firstNumber = getCurrentNumber();

                operator = value;

                expression.setText(
                    format(firstNumber) + " " + operator
                );

                startNewNumber = true;
            }

            // حساب النتيجة
            else if (value.equals("=")) {

                if (!operator.isEmpty()) {

                    double secondNumber = getCurrentNumber();

                    double result = 0;

                    expression.setText(
                        format(firstNumber) + " " +
                        operator + " " +
                        format(secondNumber) + " ="
                    );

                    switch (operator) {

                        case "+":
                            result = firstNumber + secondNumber;
                            break;

                        case "-":
                            result = firstNumber - secondNumber;
                            break;

                        case "×":
                            result = firstNumber * secondNumber;
                            break;

                        case "÷":

                            if (secondNumber == 0) {

                                display.setText("Error");
                                operator = "";
                                startNewNumber = true;
                                return;
                            }

                            result = firstNumber / secondNumber;
                            break;
                    }

                    if (!Double.isFinite(result)) {

                        display.setText("Error");

                    } else {

                        display.setText(format(result));
                    }

                    operator = "";
                    startNewNumber = true;
                }
            }

        } catch (NumberFormatException ex) {

            display.setText("Error");
            operator = "";
            startNewNumber = true;
        }
    }

    // قراءة الرقم الحالي
    private double getCurrentNumber() {

        if (display.getText().equals("Error")) {
            return 0;
        }

        return Double.parseDouble(display.getText());
    }

    // تنسيق الأرقام والنتائج
    private String format(double number) {

        if (!Double.isFinite(number)) {
            return "Error";
        }

        return BigDecimal.valueOf(number)
            .stripTrailingZeros()
            .toPlainString();
    }

    public static void main(String[] args) {

        launch(args);
    }
}
