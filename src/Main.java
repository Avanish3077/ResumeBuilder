import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    private File selectedImageFile = null;

    @Override
    public void start(Stage primaryStage) {
        // Labels and TextFields
        TextField nameField = new TextField();
        nameField.setPromptText("Full Name");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone Number");

        TextField schoolField = new TextField();
        schoolField.setPromptText("School Name");

        TextField collegeField = new TextField();
        collegeField.setPromptText("College Name");

        TextArea profileSummaryArea = new TextArea();
        profileSummaryArea.setPromptText("Profile Summary");

        TextField internshipField = new TextField();
        internshipField.setPromptText("Internship Company");

        TextArea internshipDetails = new TextArea();
        internshipDetails.setPromptText("Internship Details");

        TextField projectField = new TextField();
        projectField.setPromptText("Project Name");

        TextArea projectDetails = new TextArea();
        projectDetails.setPromptText("Project Details");

        TextArea skillsArea = new TextArea();
        skillsArea.setPromptText("Skills (comma separated)");

        // Upload Profile Photo Button
        Button photoBtn = new Button("Upload Profile Photo");
        photoBtn.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Profile Picture");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
            );
            selectedImageFile = fileChooser.showOpenDialog(primaryStage);
        });

        // Button to generate resume
        Button generateBtn = new Button("Generate Resume");
        generateBtn.setOnAction(e -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();
            String school = schoolField.getText();
            String college = collegeField.getText();
            String profileSummary = profileSummaryArea.getText();
            String internship = internshipField.getText();
            String internshipDesc = internshipDetails.getText();
            String project = projectField.getText();
            String projectDesc = projectDetails.getText();
            String skills = skillsArea.getText();

            try {
                Document document = new Document();
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save Resume");
                fileChooser.setInitialFileName("Resume_" + name + ".pdf");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
                File file = fileChooser.showSaveDialog(primaryStage);

                if (file == null) {
                    return; // User cancelled save
                }

                PdfWriter.getInstance(document, new FileOutputStream(file));
                document.open();

                // Profile Photo
                if (selectedImageFile != null) {
                    Image img = Image.getInstance(selectedImageFile.getAbsolutePath());
                    img.scaleToFit(80, 80);
                    img.setAlignment(Element.ALIGN_LEFT);
                    document.add(img);
                }

                // Title
                Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20);
                Paragraph title = new Paragraph(name, titleFont);
                title.setAlignment(Element.ALIGN_CENTER);
                document.add(title);
                document.add(new Paragraph("Email: " + email + " | Phone: " + phone));
                document.add(new Paragraph("\n"));

                // Education
                document.add(new Paragraph("EDUCATION", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
                document.add(new Paragraph("School: " + school));
                document.add(new Paragraph("College: " + college));
                document.add(new Paragraph("\n"));

                // Profile Summary
                document.add(new Paragraph("PROFILE SUMMARY", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
                document.add(new Paragraph(profileSummary));
                document.add(new Paragraph("\n"));

                // Internship
                document.add(new Paragraph("INTERNSHIP", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
                document.add(new Paragraph("Company: " + internship));
                document.add(new Paragraph(internshipDesc));
                document.add(new Paragraph("\n"));

                // Project
                document.add(new Paragraph("PROJECT", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
                document.add(new Paragraph("Project Name: " + project));
                document.add(new Paragraph(projectDesc));
                document.add(new Paragraph("\n"));

                // Skills
                document.add(new Paragraph("SKILLS", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
                for (String skill : skills.split(",")) {
                    document.add(new Paragraph("• " + skill.trim()));
                }

                document.close();

                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(file);
                }

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Resume PDF generated successfully!");
                alert.showAndWait();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Layout
        VBox root = new VBox(10);
        root.setPadding(new javafx.geometry.Insets(15));
        root.getChildren().addAll(nameField, photoBtn, emailField, phoneField, schoolField, collegeField, profileSummaryArea, internshipField, internshipDetails, projectField, projectDetails, skillsArea, generateBtn);

        // Scene and Stage
        Scene scene = new Scene(root, 450, 700);
        primaryStage.setTitle("Resume Builder");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
