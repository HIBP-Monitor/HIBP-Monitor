package ch.bfh.project1.pwnd.jobs;


import ch.bfh.project1.pwnd.MonitoringDemon;
import ch.bfh.project1.pwnd.dao.Account;
import ch.bfh.project1.pwnd.dao.Breach;
import ch.bfh.project1.pwnd.db.*;
import ch.bfh.project1.pwnd.enums.AppInfoKey;
import ch.bfh.project1.pwnd.listener.MonitoringDemonNotifier;
import ch.bfh.project1.pwnd.utils.DateFormatter;
import ch.bfh.project1.pwnd.utils.HIBPClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;

public class CronJob implements Job {

    private static final Logger logger = LogManager.getLogger(CronJob.class);

    @Override
    public void execute(JobExecutionContext context) {
        logger.info("executing CronJob");
        MonitoringDemonNotifier demonNotifier = MonitoringDemonNotifier.getInstance();
        updateTypes();
        List<Account> accounts = AccountHandler.findAllAccounts();
        for (Account account : accounts) {
            handleHIBPResponse(account.getEmail());
            AppInfoHandler.updateAppInfo(AppInfoKey.LAST_API_CALL.name, DateFormatter.getCurrentTimestamp());
            demonNotifier.notifyApiCallListener();
            try {
                //buffer api calls, else we often get a 429
                //ref: https://support.haveibeenpwned.com/hc/en-au/articles/5744766972431-Why-do-I-keep-getting-HTTP-429-Too-Many-Requests-when-querying-within-the-rate-limit
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                logger.error(e.getMessage(),e);
                throw new RuntimeException(e);
            }
        }
        List<String> unhanded = AccountBreachHandler.findAllUnhandeld();
        if(!unhanded.isEmpty()){
            demonNotifier.notifyBreachUpdateListeners("You have " + unhanded.size() + " unhandled breaches!");
        }
    }

    private Breach[] handleHIBPResponse(String email) {
        ObjectMapper objectMapper = new ObjectMapper();
        logger.info("Calling API for account {}",email);
        HttpResponse<String> response = HIBPClient.callAPIForAccount(email);
        Breach[] breaches;
        try {
            if (response != null) {
                AppInfoHandler.incrementAPICallsCount();
                breaches = objectMapper.readValue(response.body(), Breach[].class);
                BreachHandler.addAPIBreaches(breaches);
                Arrays.stream(breaches).forEach(breach ->
                        BreachTypeHandler.addBreachType(
                                breach.getDataClasses().toArray(new String[0]),
                                breach.getBreachName()
                        )
                );
                AccountBreachHandler.addAccountBreach(Arrays.stream(breaches).map(Breach::getBreachName).toList().toArray(new String[0]), email);
                return breaches;
            }
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage(),e);
            throw new RuntimeException(e);
        }
        return new Breach[]{};
    }

    private void updateTypes() {
        HttpResponse<String> response = HIBPClient.callAPIForTypes();
        if (response != null) {
            String[] foundTypes = response.body()
                    .replace("[", "")
                    .replace("]", "")
                    .replace("\"", "")
                    .split(",");
            TypeHandler.addTypes(foundTypes);
        }
    }
}

