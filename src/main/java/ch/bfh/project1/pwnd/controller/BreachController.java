package ch.bfh.project1.pwnd.controller;

import ch.bfh.project1.pwnd.dao.Account;
import ch.bfh.project1.pwnd.db.AccountBreachHandler;
import ch.bfh.project1.pwnd.model.AccountBreachModel;
import ch.bfh.project1.pwnd.model.BreachModel;
import ch.bfh.project1.pwnd.views.AccountBreachView;
import ch.bfh.project1.pwnd.views.BreachView;
import ch.bfh.project1.pwnd.views.MainFrame;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.util.List;

public class BreachController {

    private final BreachView breachView;
    private final BreachModel breachModel;
    private final MainFrame mainFrame;
    private final Account previousAccount;
    private static final Logger logger = LogManager.getLogger(BreachController.class);


    public BreachController(BreachView breachView, BreachModel breachModel, MainFrame mainFrame, Account account) {
        this.breachView = breachView;
        this.breachModel = breachModel;
        this.mainFrame = mainFrame;
        this.previousAccount = account;
        initializeView();
        registerListeners();
    }

    private void initializeView() {
        breachView.setName(breachModel.getBreach().getBreachName());
        this.setDomain(breachModel.getBreach().getDomain());
        setBreachDate(breachModel.getBreach().getBreachDate());
        setAddedDate(breachModel.getBreach().getAddedDate());
        setModifiedDate(breachModel.getBreach().getModifiedDate());
        setPwnCount(String.valueOf(breachModel.getBreach().getPwnCount()));
        setDescription(breachModel.getBreach().getDescription());
        setBreachTypes(breachModel.getBreachTypes());
        setAffectedAccounts(breachModel.getAffectedAccounts());
    }

    private void registerListeners() {
        breachView.getBackButton().addActionListener(e -> switchBackToAccountBreachView());
    }

    private void switchBackToAccountBreachView() {
        AccountBreachModel accountBreachModel = new AccountBreachModel(previousAccount, AccountBreachHandler.findAllBreachesByAccount(previousAccount.getEmail()));
        AccountBreachView accountBreachView = new AccountBreachView();
        AccountBreachController accountBreachController = new AccountBreachController(accountBreachView, accountBreachModel, this.mainFrame);
        mainFrame.getContentPane().removeAll();
        mainFrame.getContentPane().add(accountBreachView);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    public void setTitle(String title) {
        this.breachView.getNameLabel().setText("<html><b>Title:</b> " + title + "</html>");
    }

    public void setDomain(String domain) {
        this.breachView.getDomainLabel().setText("<html><b>Domain:</b> " + domain + "</html>");
    }

    public void setBreachDate(String breachDate) {
        this.breachView.getBreachDateLabel().setText("<html><b>Breach Date:</b> " + breachDate + "</html>");
    }

    public void setAddedDate(String addedDate) {
        this.breachView.getAddedDateLabel().setText("<html><b>Added Date:</b> " + addedDate + "</html>");
    }

    public void setModifiedDate(String modifiedDate) {
        this.breachView.getModifiedDateLabel().setText("<html><b>Modified Date:</b> " + modifiedDate + "</html>");
    }

    public void setPwnCount(String pwnCount) {
        this.breachView.getPwnCountLabel().setText("<html><b>Total affected Emails:</b> " + pwnCount + "</html>");
    }


    public void setDescription(String description) {
        JPanel descriptionPanel = (JPanel) this.breachView.getDescriptionScrollPane().getViewport().getView();
        descriptionPanel.removeAll();

        JEditorPane descriptionPane = new JEditorPane();
        descriptionPane.setContentType("text/html");
        descriptionPane.setEditable(false);

        // Use inline styles to enforce word wrapping
        descriptionPane.setText("<html><body style='width:450px; font-family:Arial, sans-serif;'>"
                + description
                + "</body></html>");

        descriptionPane.addHyperlinkListener(e -> {
            if (e.getEventType() == HyperlinkEvent.EventType.ENTERED) {
                descriptionPane.setToolTipText(e.getURL().toString());
            } else if (e.getEventType() == HyperlinkEvent.EventType.EXITED) {
                descriptionPane.setToolTipText(null);
            } else if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                try {
                    Desktop.getDesktop().browse(e.getURL().toURI());
                } catch (Exception ex) {
                    logger.error(ex.getMessage());
                    JOptionPane.showMessageDialog(null, "Failed to open link: " + ex.getMessage());
                }
            }
        });

        // Add the JEditorPane to the description panel
        descriptionPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Add some padding
        descriptionPanel.setLayout(new BorderLayout()); // Use BorderLayout to fit content correctly
        descriptionPanel.add(descriptionPane, BorderLayout.CENTER);

        descriptionPanel.revalidate();
        descriptionPanel.repaint();
    }



    public void setBreachTypes(java.util.List<String> breachTypes) {
        JPanel breachTypesPanel = (JPanel) this.breachView.getBreachTypesScrollPane().getViewport().getView();
        breachTypesPanel.removeAll();

        for (String type : breachTypes) {
            JLabel label = new JLabel(type);
            label.setAlignmentX(Component.LEFT_ALIGNMENT);
            breachTypesPanel.add(label);
        }
        breachTypesPanel.revalidate();
        breachTypesPanel.repaint();
    }

    public void setAffectedAccounts(List<String> affectedAccounts) {
        JPanel affectedAccountsPanel = (JPanel) this.breachView.getAffectedAccountsScrollPane().getViewport().getView();
        affectedAccountsPanel.removeAll();

        for (String account : affectedAccounts) {
            JLabel label = new JLabel(account);
            label.setAlignmentX(Component.LEFT_ALIGNMENT);
            affectedAccountsPanel.add(label);
        }
        affectedAccountsPanel.revalidate();
        affectedAccountsPanel.repaint();
    }


}
