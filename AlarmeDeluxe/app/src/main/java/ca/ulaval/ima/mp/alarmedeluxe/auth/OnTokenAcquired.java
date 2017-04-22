package ca.ulaval.ima.mp.alarmedeluxe.auth;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Intent;
import android.os.Bundle;

import java.io.IOException;

public class OnTokenAcquired  implements AccountManagerCallback<Bundle> {

    private AsyncTokenRequest asyncTokenRequest;

    public interface AsyncTokenRequest {
        void processFinish(Bundle bundle);
    }

    public OnTokenAcquired(AsyncTokenRequest asyncTokenRequest) {
        this.asyncTokenRequest = asyncTokenRequest;
    }

    @Override
    public void run(AccountManagerFuture<Bundle> result) {
        // Get the result of the operation from the AccountManagerFuture.
        Bundle bundle = null;
        try {
            bundle = result.getResult();

            Intent launch = (Intent) result.getResult().get(AccountManager.KEY_INTENT);
            if (launch != null) {
                bundle.putParcelable("intent", launch);
            }
        } catch (OperationCanceledException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AuthenticatorException e) {
            e.printStackTrace();
        }

        // The token is a named value in the bundle. The name of the value
        // is stored in the constant AccountManager.KEY_AUTHTOKEN.
        String token = bundle.getString(AccountManager.KEY_AUTHTOKEN);
        bundle.putString("token", "token");

        asyncTokenRequest.processFinish(bundle);
    }
}
