import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;
import java.awt.event.*;

public class Calculator{
    private static final int frameWidth = 240;
    private static final int frameHeight = 320;

    public static void main(String args[]){
        JFrame frame = new JFrame();
        frame.setTitle("Calculator");
        ImageIcon CalculatorIcon = new ImageIcon("Image\\CalculatorIcon.png");
        frame.setIconImage(CalculatorIcon.getImage());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(frameWidth, frameHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());
        
        frame.add(new CalculatorPanel(), BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }
}

class CalculatorPanel extends JPanel implements ActionListener, MouseListener{
    private static final long serialVersionUID = 1L;
    private final int panelWidth = 220;
    private final int panelHeight = 260;
    private final int descriptionBoxFontSize = 15;
    private final int valueBoxFontSize = 20;
    private final int buttonWidth = 50;
    private final int buttonHeight = 30;
    private final int buttonFontSize = 15;
    private double firstNumber = 0.0, secondNumber = 0.0, result = 0.0;
    private String operator  = "", clickedButtonType = "", Message = "Error";

    private GridBagLayout layout;
    private GridBagConstraints gbc;

    //Here, array of digitButton, mathematicalButton and OperationButton.
    private JButton[] digitButton, arithmeticButton, mathematicalButton;  
    String digitButtonSymbol[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "."};
    String mathematicalSymbol[] = {"x\u00b2", "x\u02b8", "\u221A", "x!"};
    String arithmeticSymbol[] = {"C", "Del", "/", "*", "-", "+", "="}; 
    JTextField descriptionBox, valueBox;

    //Here define all buttonConstraints----> [0]=gridx, [1]=gridy, [2]=gridwidth, [3]=gridheight.
    private int[] digitButtonXAxis = {1, 0, 1, 2, 0, 1, 2, 0, 1, 2, 0};
    private int[] digitButtonYAxis = {7, 6, 6, 6, 5, 5, 5, 4, 4, 4, 7};
    private int[] digitButtonWidth = {2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
    private int[] digitButtonHeight = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
    
    private int[] mathButtonXAxis = {0, 1, 2, 0};
    private int[] mathButtonYAxis = {3, 3, 3, 2};
    private int[] mathButtonWidth = {1, 1, 1, 1};
    private int[] mathButtonHeight = {1, 1, 1, 1};

    private int[] arithmeticButtonXAxis = {1, 2, 3, 3, 3, 3, 3};
    private int[] arithmeticButtonYAxis = {2, 2, 2, 3, 4, 5, 6};
    private int[] arithmeticButtonWidth = {1, 1, 1, 1, 1, 1, 1};
    private int[] arithmeticButtonHeight = {1, 1, 1, 1, 1, 1, 2};

    private int[] textBoxXAxis = {0, 0};
    private int[] textBoxYAxis = {1, 0};
    private int[] textBoxWidth = {4, 4};
    private int[] textBoxHeight = {1, 1};

    private int[][] setupConstraints(int[] x, int[] y, int[] width, int[] height) {
        int[][] constraints = new int[x.length][4];
        for (int i = 0; i < x.length; i++) {
            constraints[i][0] = x[i];
            constraints[i][1] = y[i];
            constraints[i][2] = width[i];
            constraints[i][3] = height[i];
        }
        return constraints;
    }

    //Here Define ButtonConstraints----> [0]=gridx, [1]=gridy, [2]=gridwidth, [3]=gridheight.
    private int[][] digitConstraints = setupConstraints(digitButtonXAxis, digitButtonYAxis, digitButtonWidth, digitButtonHeight);
    private int[][] mathematicalConstraints = setupConstraints(mathButtonXAxis, mathButtonYAxis, mathButtonWidth, mathButtonHeight);                                             
    private int[][] arithmeticConstraints = setupConstraints(arithmeticButtonXAxis, arithmeticButtonYAxis, arithmeticButtonWidth, arithmeticButtonHeight);
    private int [][] boxConstraints = setupConstraints(textBoxXAxis, textBoxYAxis, textBoxWidth, textBoxHeight);

    //class Constructor.
    public CalculatorPanel(){
        setPanelProperties();//Invoked setPanelProperties Method.
        addGuiComponent(); //Invoked addGuiComponent Method.
        addActionAndMouseListeners(digitButton);
        addActionAndMouseListeners(mathematicalButton);
        addActionAndMouseListeners(arithmeticButton);
    }

    //This method set Panel Properties.
    private void setPanelProperties(){
        setPreferredSize(new Dimension(panelWidth, panelHeight));
        setBackground(new Color(0, 0, 0));
        layout =  new GridBagLayout();
        gbc = new GridBagConstraints();
        setLayout(layout);
        layout.columnWidths = new int[]{buttonWidth, buttonWidth, buttonWidth, buttonWidth};
        layout.rowHeights = new int[]{buttonHeight, buttonHeight, buttonHeight, buttonHeight, buttonHeight, buttonHeight, buttonHeight,buttonHeight};
    }

    //Create GUI.
    private void addGuiComponent(){
        //DigitButton.
        digitButton = createButtons(digitButtonSymbol, new Color(255, 255, 255), new Color(0, 0, 0), digitConstraints, buttonFontSize);

        //MathematicalButton.
        mathematicalButton = createButtons(mathematicalSymbol, new Color(255, 255, 255), new Color(0, 0, 0), mathematicalConstraints, buttonFontSize);

        //OperationButton.
        arithmeticButton = createButtons(arithmeticSymbol, new Color(255, 255, 255), new Color(0, 0, 255),  arithmeticConstraints, buttonFontSize);

        //descriptionBox
        descriptionBox =  createInputVisibleBox("", new Color(0, 0, 0), new Color(255, 255, 255), descriptionBoxFontSize, boxConstraints[1][0], boxConstraints[1][1], boxConstraints[1][2], boxConstraints[1][3]);

        //valueBox
        valueBox =  createInputVisibleBox("0", new Color(0, 0, 0), new Color(255, 255, 255), valueBoxFontSize, boxConstraints[0][0], boxConstraints[0][1], boxConstraints[0][2], boxConstraints[0][3]);
    }
 
    //This method apply Action and Mouse Listener.
    private void addActionAndMouseListeners(JButton[] buttons){
        for(int i=0; i<buttons.length; i++){
            buttons[i].addActionListener(this);
            buttons[i].addMouseListener(this);
        }
    }

    //This method help of style and position on component.
    private void applyStylesPositionOnComponent(JComponent component, Color backgroundColor, Color foregroundColor, int gridX, int gridY, int gridWidth, int gridHeight, int fontSize){
        component.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, fontSize));
        component.setBackground(backgroundColor);
        component.setForeground(foregroundColor);

        gbc.gridx = gridX;
        gbc.gridy = gridY;
        gbc.gridwidth = gridWidth;
        gbc.gridheight = gridHeight;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(4, 4, 4, 4);
        add(component, gbc);//add Text in Panel.
    }

    //This Method create buttons.
    private JButton[] createButtons(String[] buttonNames, Color backgroundColor, Color foregroundColor, int[][] constraints, int fontSize){
        JButton[] buttons =  new JButton[buttonNames.length];
        for(int i=0; i<buttonNames.length; i++){
            buttons[i] = new JButton(buttonNames[i]);
            applyStylesPositionOnComponent(buttons[i], backgroundColor, foregroundColor, constraints[i][0], constraints[i][1], constraints[i][2], constraints[i][3], fontSize);
            buttons[i].setBorder(new BevelBorder(BevelBorder.RAISED));
            buttons[i].setFocusable(false);
            buttons[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        return buttons;
    }

    //This method create InputBox.
    private JTextField createInputVisibleBox(String text, Color backgroundColor, Color foregroundColor, int fontSize, int gridX, int gridY, int gridWidth, int gridHeight){
        JTextField box =  new JTextField(text);
        applyStylesPositionOnComponent(box, backgroundColor, foregroundColor, gridX, gridY, gridWidth, gridHeight, fontSize);
        box.setHorizontalAlignment(JTextField.RIGHT);
        box.setEditable(false);
        box.setBorder(null);
        return box;
    }

    //This Method set First Number.
    private void setFirstNumber(double firstNumber){
        this.firstNumber = firstNumber;
    }

    //This Method set Second Number.
    private void setSecondNumber(double secondNumber){
        this.secondNumber = secondNumber;
    }

    private void setValueBoxText(String text){
        valueBox.setText(text.equals(Message) ? String.valueOf(firstNumber) : text);
    }

    //This method store operator.
    private void storeOperator(String operation, String firstValue){
        descriptionBox.setText(operation.equals("sqrt") ? operation+"("+firstValue+")" : firstValue+operation);
    }

    //This Method Remove decimal number from the end.
    private String checkDecimal(String suffix){
        return suffix.endsWith(".0") ? suffix.replace(".0", "") : suffix;
    }

    //This Method calculate two number.
    private double calculateNumber(double firstValue, double secondValue, String operatorValue){
        double calculateResult = 0.0;
        if(operatorValue.equals("x^y")){
            calculateResult = calculateSquare(firstValue, secondValue);
        }
        else if(operatorValue.equals("/")){
            calculateResult = firstValue / secondValue;
        }
        else if(operatorValue.equals("*")){
            calculateResult = firstValue * secondValue;
        }
        else if(operatorValue.equals("-")){
            calculateResult = firstValue - secondValue;
        }
        else if(operatorValue.equals("+")){
            calculateResult = firstValue + secondValue;
        }
        return calculateResult;
    }

    //This method handle clicked digit buttons.
    private void handleDigitButtonClick(String clickedButtonText){
        if(clickedButtonText.equals(".")){//handle dot button.
            if(clickedButtonType.equals("arithmeticButton") || clickedButtonType.equals("mathButton") || valueBox.getText().equals(Message)){
                valueBox.setText("0");
            }

            if(valueBox.getText().contains(".")){
                return;
            }
        }

        if(clickedButtonText.matches("[0-9]")){//handle digit(0-9).
            if(clickedButtonType.equals("arithmeticButton") || clickedButtonType.equals("mathButton") || clickedButtonType.isEmpty() || valueBox.getText().equals(Message)){
                valueBox.setText("");
            }
        }
        setValueBoxText(valueBox.getText().concat(clickedButtonText));
    }

    //This Method erase all data.
    private void handleClearButtonClick(){
        valueBox.setText("0");
        descriptionBox.setText("");    
        operator = "";
        clickedButtonType = "";
        Message = "Error";
    }

    //This Method Delete only one data.
    private void handleDeleteButtonClick(String string){
        if(string.length() == 1){
            setValueBoxText("0");
            return;
        }
        setValueBoxText("");
        for(int i=0; i<string.length()-1; i++){
            setValueBoxText(valueBox.getText()+string.charAt(i));
        }
    }

    //This Method handle two number calculation.
    private void handleEqualButtonClick(){
        if(operator.isEmpty() || valueBox.getText().equals(Message)){
            return;
        }
        setSecondNumber(Double.valueOf(valueBox.getText()));
        result = calculateNumber(firstNumber, secondNumber, operator);  
        descriptionBox.setText("");
        setValueBoxText(checkDecimal(Double.toString(result)));
    }
    //This method handle clicked Arithmetic buttons.
    private void handleArithmeticButtonClick(String clickedButtonText){  
        if(clickedButtonText.equals("C")){
            handleClearButtonClick();
        }
        else if(clickedButtonText.equals("Del")){
            handleDeleteButtonClick(valueBox.getText());
        }
        else if(clickedButtonText.equals("=")){
            handleEqualButtonClick();
        }
        else{
            operator = clickedButtonText;
            setValueBoxText(valueBox.getText());
            setFirstNumber(Double.valueOf(valueBox.getText()));
            storeOperator(operator, checkDecimal(String.valueOf(firstNumber)));
        }
    }

    //This Method calculate Factorial number.
    private int calculateFactorial(int number){
        if(number == 1 || number == 0){
            return 1;
        }
        return number*calculateFactorial(number-1);
    }

    //This method calculate Square value.
    private double calculateSquare(double n, double exponent){
        return Math.pow(n, exponent);
    } 

    //This Method calculate square root value.
    private double calculateSquareRoot(double number){
        return Math.sqrt(number);
    }

    //This method handle clicked Math buttons.
    private void handleMathButtonClick(String clickedButtonText) {
        setValueBoxText(valueBox.getText());
        setFirstNumber(Double.valueOf(valueBox.getText()));
        String mathResult = "";

        switch (clickedButtonText) {
            case "x\u02b8"://
                operator = "x^y";
                storeOperator("^", checkDecimal(String.valueOf(firstNumber)));
                break;
            case "x\u00b2":
                storeOperator("^2", checkDecimal(String.valueOf(firstNumber)));
                mathResult = String.valueOf(calculateSquare(firstNumber, 2));
                break;
            case "\u221A":
                storeOperator("sqrt", checkDecimal(String.valueOf(firstNumber)));
                mathResult = String.valueOf(calculateSquareRoot(firstNumber));
                break;
            case "x!":
                storeOperator("!", checkDecimal(String.valueOf(firstNumber)));
                if (firstNumber % 1 == 0) {
                    String prefix = "-";
                    //This statement execute when firstNumber start with negative.
                    if(String.valueOf(firstNumber).startsWith(prefix)){
                        firstNumber = Double.valueOf(String.valueOf(firstNumber).replaceFirst(prefix, ""));
                        mathResult = mathResult.concat(prefix);
                    }
                    mathResult = mathResult.concat(String.valueOf(calculateFactorial((int) firstNumber)));
                } else {
                    mathResult = Message;
                }
                break;
        }

        if(!operator.equals("x^y")){
            valueBox.setText(checkDecimal(String.valueOf(mathResult)));
        }
    }

    //Here, Perform ActionEvent.
    public void actionPerformed(ActionEvent e){
        try{
            JButton sourceButton = (JButton) e.getSource();
            String clickedButtonText = sourceButton.getText();                

            if(clickedButtonText.matches("[0-9.]*")){//Digit Button(0-9)
                handleDigitButtonClick(clickedButtonText);
                clickedButtonType = "button";
            }
            else if(clickedButtonText.matches("[+\\-/* C Del =]*")){//OperationButton(/, *, -, +, C, Del, =).
                handleArithmeticButtonClick(clickedButtonText);
                clickedButtonType = "arithmeticButton";
            }
            else if(clickedButtonText.matches("[x\u00b2 | x\u02b8 | \u221A | x!]*")){//MathematicalButton.(x^2, x^y, sqrt, !).
                handleMathButtonClick(clickedButtonText);
                clickedButtonType = "mathButton";
            }
        }
        catch(ArithmeticException | NumberFormatException exception){
            valueBox.setText(Message + exception.getMessage());
        }
    } 

    public void mouseClicked(MouseEvent mouseClickedEvent){
        performMouseClickedEvent(mouseClickedEvent, digitButton, new Color(242, 247, 131));
        performMouseClickedEvent(mouseClickedEvent, mathematicalButton, new Color(242, 247, 131));
        performMouseClickedEvent(mouseClickedEvent, arithmeticButton, new Color(242, 247, 131));
    }
    public void mouseExited(MouseEvent mouseClickedEvent){
        performMouseClickedEvent(mouseClickedEvent, digitButton, new Color(255, 255, 255));
        performMouseClickedEvent(mouseClickedEvent, mathematicalButton, new Color(255, 255, 255));
        performMouseClickedEvent(mouseClickedEvent, arithmeticButton, new Color(255, 255, 255));
    }
    public void mouseEntered(MouseEvent mouseClickedEvent){
        performMouseClickedEvent(mouseClickedEvent, digitButton, new Color(242, 247, 131));
        performMouseClickedEvent(mouseClickedEvent, mathematicalButton, new Color(242, 247, 131));
        performMouseClickedEvent(mouseClickedEvent, arithmeticButton, new Color(242, 247, 131));
    }
    public void mouseReleased(MouseEvent mouseClickedEvent){
        performMouseClickedEvent(mouseClickedEvent, digitButton, new Color(255, 255, 255));
        performMouseClickedEvent(mouseClickedEvent, mathematicalButton, new Color(255, 255, 255));
        performMouseClickedEvent(mouseClickedEvent, arithmeticButton, new Color(255, 255, 255));
    }
    public void mousePressed(MouseEvent mouseClickedEvent){
        performMouseClickedEvent(mouseClickedEvent, digitButton, new Color(0, 0, 0));
        performMouseClickedEvent(mouseClickedEvent, mathematicalButton, new Color(0, 0, 0));
        performMouseClickedEvent(mouseClickedEvent, arithmeticButton, new Color(0, 0, 0));
    }

    public void performMouseClickedEvent(MouseEvent event, JButton buttons[], Color color){
        for(int i=0; i<buttons.length; i++){
            if(event.getSource() == buttons[i]){
                buttons[i].setBackground(color);
            }
        }
    }
}