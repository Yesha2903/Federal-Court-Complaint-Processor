import java.io.File;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

class StateComplaintException extends Exception {
    public StateComplaintException(String message) {
        super(message);
    }
}

class Complaint {
    private String causeOfAction;
    private String plaintiffCitizenship;
    private String defendantCitizenship;
    private String originalStateOfFiling;
    private double amountInControversy;
    private int id;
    private static int nextID = 1;

    public Complaint(String causeOfAction, double amountInControversy, String plaintiffCitizenship, String defendantCitizenship, String originalStateOfFiling) {
        this.causeOfAction = causeOfAction;
        this.amountInControversy = amountInControversy;
        this.plaintiffCitizenship = plaintiffCitizenship;
        this.defendantCitizenship = defendantCitizenship;
        this.originalStateOfFiling = originalStateOfFiling;
        this.id = nextID;
        nextID++;
    }

    public String getCauseOfAction() {
        return causeOfAction;
    }

    public String getPlaintiffCitizenship() {
        return plaintiffCitizenship;
    }

    public String getDefendantCitizenship() {
        return defendantCitizenship;
    }

    public String getOriginalStateOfFiling() {
        return originalStateOfFiling;
    }

    public double getAmountInControversy() {
        return amountInControversy;
    }

    public int getId() {
        return id;
    }
}

public class Assignment6 {
    public static void processComplaint(Complaint c) throws StateComplaintException {
        String causeOfAction = c.getCauseOfAction();
        String plaintiffCitizenship = c.getPlaintiffCitizenship();
        String defendantCitizenship = c.getDefendantCitizenship();
        double amountInControversy = c.getAmountInControversy();
        String originalStateOfFiling = c.getOriginalStateOfFiling();

        if (causeOfAction.equals("Equal Protection Challenge") || causeOfAction.equals("Title IX Workplace Discrimination")
                || causeOfAction.equals("Prisoner Civil Rights Claim") || causeOfAction.equals("Fair Labor Standard Act Claim")) {
            return; // Valid federal complaint
        }

        if (plaintiffCitizenship.equals(defendantCitizenship)) {
            throw new StateComplaintException("Lack of Diversity");
        }

        if (amountInControversy <= 75000) {
            throw new StateComplaintException("Amount in controversy less than or equal to $75000");
        }

        if (defendantCitizenship.equals(originalStateOfFiling)) {
            throw new StateComplaintException("No prejudice through diversity");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String inputFileName = "C:\\Users\\yesha\\OneDrive\\Desktop\\KSU\\CSE 1322 L\\complaints.txt";
//        String acceptedFileName = "C:\\Users\\yesha\\OneDrive\\Desktop\\KSU\\CSE 1322 L\\accepted.txt";
//        String remandedFileName = "C:\\Users\\yesha\\OneDrive\\Desktop\\KSU\\CSE 1322 L\\remanded.txt";
        String acceptedFileName = "accepted.txt";
        String remandedFileName = "remanded.txt";

        System.out.print("[Federal Court Complaint Processor]\nEnter file name to process: ");
        String userInput = scanner.nextLine();

        if (userInput.equals("complaints.txt")) {
            // User input matches the expected input file name
            try {
                File inputFile = new File(inputFileName);
                Scanner fileScanner = new Scanner(inputFile);
                FileWriter acceptedFile = new FileWriter(acceptedFileName);
                FileWriter remandedFile = new FileWriter(remandedFileName);
                int acceptedCount = 0;
                int remandedCount = 0;

                while (fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine();
                    String[] data = line.split(",");

                    String causeOfAction = data[0];
                    double amountInControversy = Double.parseDouble(data[1]);
                    String plaintiffCitizenship = data[2];
                    String defendantCitizenship = data[3];
                    String originalStateOfFiling = data[4];

                    Complaint complaint = new Complaint(causeOfAction, amountInControversy, plaintiffCitizenship, defendantCitizenship, originalStateOfFiling);

                    try {
                        processComplaint(complaint);
                        // Valid federal complaint
                        acceptedFile.write("Case ID: " + complaint.getId() + "\n");
                        acceptedFile.write("Cause of action: " + complaint.getCauseOfAction() + "\n");
                        acceptedFile.write("Amount in Controversy: $" + complaint.getAmountInControversy() + "\n");
                        acceptedFile.write("Plaintiff's Citizenship: " + complaint.getPlaintiffCitizenship() + "\n");
                        acceptedFile.write("Defendant's Citizenship: " + complaint.getDefendantCitizenship() + "\n");
                        acceptedFile.write("Originally filed in: " + complaint.getOriginalStateOfFiling() + "\n");
                        acceptedFile.write("==============================\n");
                        acceptedCount++;
                    } catch (StateComplaintException e) {
                        // Invalid federal complaint
                        remandedFile.write("Case ID: " + complaint.getId() + "\n");
                        remandedFile.write("Cause of action: " + complaint.getCauseOfAction() + "\n");
                        remandedFile.write("Amount in Controversy: $" + complaint.getAmountInControversy() + "\n");
                        remandedFile.write("Plaintiff's Citizenship: " + complaint.getPlaintiffCitizenship() + "\n");
                        remandedFile.write("Defendant's Citizenship: " + complaint.getDefendantCitizenship() + "\n");
                        remandedFile.write("Originally filed in: " + complaint.getOriginalStateOfFiling() + "\n");
                        remandedFile.write("Reason for remand: " + e.getMessage() + "\n");
                        remandedFile.write("==============================\n");
                        remandedCount++;
                    }
                }

                fileScanner.close();
                acceptedFile.close();
                remandedFile.close();

                System.out.println("Processing complete. Accepted cases written to " + acceptedFileName + " and remanded cases written to " + remandedFileName );
                System.out.println("Number of remanded cases: " + remandedCount);
                System.out.println("Number of accepted cases: " + acceptedCount);

            } catch (IOException e) {
                System.out.println("Error processing the file: " + e.getMessage());
            }
        } else {
            System.out.println("No file with the name \"" + userInput + "\"");
        }
        System.out.println("Shutting down...");
    }
}