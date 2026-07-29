import java.awt.*;
java.awt.event.*;
import java.util.ArrayList;
import 
 
/*
 * ==========================================================
 * Class: Scheme
 * Purpose: Model class representing one Tamil Nadu welfare
 *          scheme along with the simple eligibility rules
 *          (age range, gender, employment status, marital
 *          status, occupation, caste category, disability)
 *          used to match it against a person's entered
 *          details. A null field means "no restriction" for
 *          that attribute (open to everyone).
 *
 *          NOTE: Real government schemes may also depend on
 *          income limits, ration card type, residency proof,
 *          etc. which this simple demo form does not collect,
 *          so results here are indicative, not official.
 * ==========================================================
 */
class Scheme {
    String name;
    String category;
    String detail;
 
    Integer minAge;      // null = no lower limit
    Integer maxAge;      // null = no upper limit
    String gender;         // "Female" / "Male" / null = any
    String employment;     // "Student" / "Employed" / "Unemployed" / null = any
    String marital;        // "Single" / "Married" / "Widowed" / null = any
    String occupation;     // "Farmer" / "Fisherman" / "Weaver" / "Construction Worker" /
    // "Sanitation Worker" / "Salt Pan Worker" / "Ex-Serviceman" /
    // "Unorganised Worker" / "None" / null = any
    String casteCategory;  // "SC/ST" / "BC/MBC/DNC" / "General" / null = any
    String disability;     // "Yes" / "No" / null = any
 
    Scheme(String name, String category, String detail, Integer minAge, Integer maxAge,
           String gender, String employment, String marital,
           String occupation, String casteCategory, String disability) {
        this.name = name;
        this.category = category;
        this.detail = detail;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.gender = gender;
        this.employment = employment;
        this.marital = marital;
        this.occupation = occupation;
        this.casteCategory = casteCategory;
        this.disability = disability;
    }
}
 
/*
 * ==========================================================
 * Class: TNSchemeEligibilityChecker
 * Purpose: AWT GUI application. User enters Name, Age,
 *          Gender, Employment Status and Marital Status.
 *          On clicking "Check Eligible Schemes", the program
 *          scans all 41 Tamil Nadu schemes and prints the
 *          ones the person likely qualifies for.
 * ==========================================================
 */
class TNSchemeEligibilityChecker extends Frame implements ActionListener {
 
    private TextField nameField, ageField;
    private Choice genderChoice, employmentChoice, maritalChoice;
    private Choice occupationChoice, casteChoice, disabilityChoice;
    private TextArea resultArea;
    private Button checkButton, clearButton, exitButton;
 
    private ArrayList<Scheme> schemes = new ArrayList<Scheme>();
 
    public TNSchemeEligibilityChecker() {
        super("Tamil Nadu Welfare Scheme Eligibility Checker");
        loadSchemes();
        buildUI();
        setSize(1000, 680);
        setVisible(true);
    }
 
    // ---------------- UI SETUP ----------------
    private void buildUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(235, 245, 235));
 
        Label heading = new Label("Tamil Nadu Welfare Scheme Eligibility Checker", Label.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 18));
        add(heading, BorderLayout.NORTH);
 
        Panel formPanel = new Panel(new GridLayout(9, 2, 8, 8));
        formPanel.setBackground(new Color(245, 250, 245));
 
        formPanel.add(new Label("Name:"));
        nameField = new TextField();
        formPanel.add(nameField);
 
        formPanel.add(new Label("Age:"));
        ageField = new TextField();
        formPanel.add(ageField);
 
        formPanel.add(new Label("Gender:"));
        genderChoice = new Choice();
        genderChoice.add("Female");
        genderChoice.add("Male");
        genderChoice.add("Other");
        formPanel.add(genderChoice);
 
        formPanel.add(new Label("Employment Status:"));
        employmentChoice = new Choice();
        employmentChoice.add("Student");
        employmentChoice.add("Employed");
        employmentChoice.add("Unemployed");
        formPanel.add(employmentChoice);
 
        formPanel.add(new Label("Marital Status:"));
        maritalChoice = new Choice();
        maritalChoice.add("Single");
        maritalChoice.add("Married");
        maritalChoice.add("Widowed");
        formPanel.add(maritalChoice);
 
        formPanel.add(new Label("Occupation Type:"));
        occupationChoice = new Choice();
        occupationChoice.add("None");
        occupationChoice.add("Farmer");
        occupationChoice.add("Fisherman");
        occupationChoice.add("Weaver");
        occupationChoice.add("Construction Worker");
        occupationChoice.add("Sanitation Worker");
        occupationChoice.add("Salt Pan Worker");
        occupationChoice.add("Ex-Serviceman");
        occupationChoice.add("Unorganised Worker");
        formPanel.add(occupationChoice);
 
        formPanel.add(new Label("Caste Category:"));
        casteChoice = new Choice();
        casteChoice.add("General");
        casteChoice.add("BC/MBC/DNC");
        casteChoice.add("SC/ST");
        formPanel.add(casteChoice);
 
        formPanel.add(new Label("Disability:"));
        disabilityChoice = new Choice();
        disabilityChoice.add("No");
        disabilityChoice.add("Yes");
        formPanel.add(disabilityChoice);
 
        Panel btnPanel = new Panel(new GridLayout(1, 3, 8, 8));
        checkButton = new Button("Check Eligible Schemes");
        clearButton = new Button("Clear");
        exitButton = new Button("Exit");
        checkButton.addActionListener(this);
        clearButton.addActionListener(this);
        exitButton.addActionListener(this);
        btnPanel.add(checkButton);
        btnPanel.add(clearButton);
        btnPanel.add(exitButton);
 
        Panel westPanel = new Panel(new BorderLayout(5, 10));
        westPanel.add(formPanel, BorderLayout.NORTH);
        westPanel.add(btnPanel, BorderLayout.SOUTH);
        add(westPanel, BorderLayout.WEST);
 
        resultArea = new TextArea("Enter your details on the left and click\n" +
                "'Check Eligible Schemes' to see matching schemes.",
                25, 55, TextArea.SCROLLBARS_VERTICAL_ONLY);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        add(resultArea, BorderLayout.CENTER);
 
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(0);
            }
        });
    }
 
    // ---------------- SCHEME DATA (all 41 schemes) ----------------
    // Constructor order: name, category, detail, minAge, maxAge,
    //                     gender, employment, marital, occupation, casteCategory, disability
    private void loadSchemes() {
        // ---- Women ----
        schemes.add(new Scheme("Kalaignar Magalir Urimai Thogai", "Women",
                "Rs.1,000/month DBT to women head of household",
                21, null, "Female", null, null, null, null, null));
        schemes.add(new Scheme("Vidiyal Payanam (Free Bus Travel)", "Women",
                "Free bus travel for all women",
                null, null, "Female", null, null, null, null, null));
        schemes.add(new Scheme("TN Free LPG Connection Scheme", "Women",
                "Free LPG connection + stove + 2 refills for women",
                18, null, "Female", null, null, null, null, null));
        schemes.add(new Scheme("TN Magalir Thittam (SHG)", "Women",
                "Rs.10,000 revolving fund + loans for women SHGs",
                18, null, "Female", null, null, null, null, null));
        schemes.add(new Scheme("Dr Muthulakshmi Reddy Maternity Benefit", "Women",
                "Rs.18,000 for pregnant women",
                18, 45, "Female", null, "Married", null, null, null));
        schemes.add(new Scheme("TN Widow Pension", "Women",
                "Rs.1,000/month for destitute widows",
                null, null, "Female", null, "Widowed", null, null, null));
        schemes.add(new Scheme("TN Marriage Assistance Schemes", "Women",
                "Rs.25,000-50,000 + 8g gold coin for brides",
                18, null, "Female", null, "Single", null, null, null));
        schemes.add(new Scheme("Amma Two-Wheeler Scheme", "Women",
                "50% subsidy on two-wheeler for working women",
                18, null, "Female", "Employed", null, null, null, null));
 
        // ---- Education ----
        schemes.add(new Scheme("Pudhumai Penn Scheme", "Education",
                "Rs.1,000/month for girl students Class 6-12",
                11, 17, "Female", "Student", null, null, null, null));
        schemes.add(new Scheme("TN CM Breakfast Scheme", "Education",
                "Free breakfast for govt school kids",
                null, 12, null, "Student", null, null, null, null));
        schemes.add(new Scheme("Naan Mudhalvan Scheme", "Education",
                "Free skill courses + Rs.1,000-3,000/month stipend",
                18, 35, null, null, null, null, null, null));
        schemes.add(new Scheme("Moovalur Ramamirtham Ammaiyar Thittam", "Education",
                "Rs.1,000/month for first-gen college girls",
                18, 25, "Female", "Student", null, null, null, null));
        schemes.add(new Scheme("Ennum Ezhuthum Mission", "Education",
                "Free tablet + literacy/numeracy support",
                11, 13, null, "Student", null, null, null, null));
        schemes.add(new Scheme("TN CM Free Laptop Scheme", "Education",
                "Free laptop for Class 11-12/college students",
                16, 22, null, "Student", null, null, null, null));
        schemes.add(new Scheme("Rajiv Gandhi Civil Abhayam Scheme", "Education",
                "Free IAS/IPS coaching",
                21, 32, null, null, null, null, null, null));
        schemes.add(new Scheme("TN BC/MBC/DNC Post-Matric Scholarship", "Education",
                "Full fees + Rs.230-1,200/month (BC/MBC/DNC students)",
                null, null, null, "Student", null, null, "BC/MBC/DNC", null));
        schemes.add(new Scheme("TN Adi Dravidar Post-Matric Scholarship", "Education",
                "Free education for SC/ST students",
                null, null, null, "Student", null, null, "SC/ST", null));
        schemes.add(new Scheme("TN BC/MBC/DNT Post-Matric Scholarship", "Education",
                "Full tuition + Rs.530-1,200/month (BC/MBC/DNT students)",
                null, null, null, "Student", null, null, "BC/MBC/DNC", null));
        schemes.add(new Scheme("TN Adi Dravidar & Tribal Hostels & Scholarships", "Education",
                "Free hostel + Rs.15-25K/year (SC/ST students)",
                null, null, null, "Student", null, null, "SC/ST", null));
        schemes.add(new Scheme("TN CM Sports Achievement Award", "Education",
                "Olympic gold Rs.3Cr + sports scholarship",
                null, null, null, null, null, null, null, null));
 
        // ---- Health ----
        schemes.add(new Scheme("Makkalai Thedi Maruthuvam", "Health",
                "Free doorstep healthcare screening",
                null, null, null, null, null, null, null, null));
        schemes.add(new Scheme("Kalaignar CM Health Insurance", "Health",
                "Rs.5 lakh cashless surgery insurance",
                null, null, null, null, null, null, null, null));
        schemes.add(new Scheme("TN Old Age Pension", "Health",
                "Rs.1,000/month for senior citizens",
                60, null, null, null, null, null, null, null));
        schemes.add(new Scheme("TN Differently Abled Pension", "Health",
                "Rs.1,500/month for persons with disabilities",
                null, null, null, null, null, null, null, "Yes"));
        schemes.add(new Scheme("Disability Welfare & Rehabilitation", "Health",
                "Free assistive devices + Rs.50K marriage + 4% reservation",
                null, null, null, null, null, null, null, "Yes"));
 
        // ---- Housing ----
        schemes.add(new Scheme("Kalaignar Kanavu Illam", "Housing",
                "1 lakh free urban houses",
                null, null, null, null, null, null, null, null));
        schemes.add(new Scheme("TN Housing Board Affordable Housing", "Housing",
                "EWS/LIG flats Rs.3-10 lakh",
                null, null, null, null, null, null, null, null));
 
        // ---- Agriculture ----
        schemes.add(new Scheme("TN CM Solar Powered Pumpset Scheme", "Agriculture",
                "80-90% subsidy on solar pumps (farmers)",
                null, null, null, null, null, "Farmer", null, null));
        schemes.add(new Scheme("TN Free Power for Farmers", "Agriculture",
                "9 hrs/day free electricity for pumpsets",
                null, null, null, null, null, "Farmer", null, null));
        schemes.add(new Scheme("Agricultural Labourers Welfare Board", "Agriculture",
                "Rs.3L accident + Rs.2,000/month pension",
                null, null, null, null, null, "Farmer", null, null));
        schemes.add(new Scheme("Uzhavar Pathukappu", "Agriculture",
                "Free seeds + fertilizer subsidy + crop insurance",
                null, null, null, null, null, "Farmer", null, null));
 
        // ---- Finance ----
        schemes.add(new Scheme("Tamil Nadu Pongal Gift Scheme", "Finance",
                "Rs.1,000 cash + rice/sugar/dhoti-saree (ration card holders)",
                null, null, null, null, null, null, null, null));
        schemes.add(new Scheme("TAHDCO SC/ST Housing & Enterprise Loan", "Finance",
                "Rs.10L loans at 4% for Adi Dravidar families",
                null, null, null, null, null, null, "SC/ST", null));
        schemes.add(new Scheme("TN CM Public Relief Fund", "Finance",
                "Emergency assistance Rs.10,000-5 lakh",
                null, null, null, null, null, null, null, null));
 
        // ---- Employment / Welfare Boards (occupation specific) ----
        schemes.add(new Scheme("Construction Workers Welfare Board", "Employment",
                "Rs.5L accident + maternity + education",
                null, null, null, null, null, "Construction Worker", null, null));
        schemes.add(new Scheme("Marine Fishermen Welfare Scheme", "Employment",
                "Rs.10L insurance + boat subsidy",
                null, null, null, null, null, "Fisherman", null, null));
        schemes.add(new Scheme("Handloom Weavers Welfare Board", "Employment",
                "Rs.2L insurance + scholarship",
                null, null, null, null, null, "Weaver", null, null));
        schemes.add(new Scheme("Unorganised Workers Social Security Scheme", "Employment",
                "Rs.2L insurance + pension",
                null, null, null, "Employed", null, "Unorganised Worker", null, null));
        schemes.add(new Scheme("Ex-Servicemen Welfare Scheme", "Employment",
                "10% job reservation + pension top-up",
                null, null, null, null, null, "Ex-Serviceman", null, null));
        schemes.add(new Scheme("Thooimai Kavalar Sanitation Workers Welfare", "Employment",
                "Rs.5L insurance + housing priority",
                null, null, null, null, null, "Sanitation Worker", null, null));
        schemes.add(new Scheme("Salt Pan Workers Welfare Scheme", "Employment",
                "Rs.3L accident + pension + housing",
                null, null, null, null, null, "Salt Pan Worker", null, null));
    }
 
    // ---------------- EVENT HANDLING ----------------
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == checkButton) {
            checkEligibility();
        } else if (src == clearButton) {
            clearForm();
        } else if (src == exitButton) {
            dispose();
            System.exit(0);
        }
    }
 
    private void checkEligibility() {
        String name = nameField.getText().trim();
        String ageText = ageField.getText().trim();
 
        if (name.isEmpty()) {
            resultArea.setText("Please enter your Name.");
            return;
        }
        int age;
        try {
            age = Integer.parseInt(ageText);
            if (age < 1 || age > 120) {
                resultArea.setText("Please enter a valid Age between 1 and 120.");
                return;
            }
        } catch (NumberFormatException ex) {
            resultArea.setText("Please enter a valid numeric Age.");
            return;
        }
 
        String gender = genderChoice.getSelectedItem();
        String employment = employmentChoice.getSelectedItem();
        String marital = maritalChoice.getSelectedItem();
        String occupation = occupationChoice.getSelectedItem();
        String caste = casteChoice.getSelectedItem();
        String disability = disabilityChoice.getSelectedItem();
 
        ArrayList<Scheme> matched = new ArrayList<Scheme>();
        ArrayList<Scheme> notMatched = new ArrayList<Scheme>();
 
        for (Scheme s : schemes) {
            if (isEligible(s, age, gender, employment, marital, occupation, caste, disability)) {
                matched.add(s);
            } else {
                notMatched.add(s);
            }
        }
 
        StringBuilder sb = new StringBuilder();
        sb.append("Name        : ").append(name).append("\n");
        sb.append("Age         : ").append(age).append("\n");
        sb.append("Gender      : ").append(gender).append("\n");
        sb.append("Status      : ").append(employment).append(", ").append(marital).append("\n");
        sb.append("Occupation  : ").append(occupation).append("\n");
        sb.append("Caste Cat.  : ").append(caste).append("\n");
        sb.append("Disability  : ").append(disability).append("\n");
        sb.append("========================================================\n\n");
 
        sb.append("SCHEMES YOU QUALIFY FOR (").append(matched.size()).append(" of ")
                .append(schemes.size()).append(")\n");
        sb.append("--------------------------------------------------------\n");
        if (matched.isEmpty()) {
            sb.append("No matching scheme found based on entered details.\n\n");
        } else {
            for (Scheme s : matched) {
                sb.append("* [").append(s.category).append("] ").append(s.name).append("\n");
                sb.append("  ").append(s.detail).append("\n\n");
            }
        }
 
        sb.append("========================================================\n");
        sb.append("NOT ELIGIBLE / DOES NOT MATCH (").append(notMatched.size()).append(")\n");
        sb.append("--------------------------------------------------------\n");
        for (Scheme s : notMatched) {
            sb.append("- [").append(s.category).append("] ").append(s.name).append("\n");
        }
 
        resultArea.setText(sb.toString());
    }
 
    private boolean isEligible(Scheme s, int age, String gender, String employment, String marital,
                               String occupation, String caste, String disability) {
        if (s.minAge != null && age < s.minAge) return false;
        if (s.maxAge != null && age > s.maxAge) return false;
        if (s.gender != null && !s.gender.equalsIgnoreCase(gender)) return false;
        if (s.employment != null && !s.employment.equalsIgnoreCase(employment)) return false;
        if (s.marital != null && !s.marital.equalsIgnoreCase(marital)) return false;
        if (s.occupation != null && !s.occupation.equalsIgnoreCase(occupation)) return false;
        if (s.casteCategory != null && !s.casteCategory.equalsIgnoreCase(caste)) return false;
        if (s.disability != null && !s.disability.equalsIgnoreCase(disability)) return false;
        return true;
    }
 
    private void clearForm() {
        nameField.setText("");
        ageField.setText("");
        genderChoice.select(0);
        employmentChoice.select(0);
        maritalChoice.select(0);
        occupationChoice.select(0);
        casteChoice.select(0);
        disabilityChoice.select(0);
        resultArea.setText("Enter your details on the left and click\n" +
                "'Check Eligible Schemes' to see matching schemes.");
    }
 
    public static void main(String[] args) {
        new TNSchemeEligibilityChecker();
    }
}
 
