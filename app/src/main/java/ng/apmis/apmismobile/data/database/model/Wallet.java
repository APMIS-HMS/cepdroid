package ng.apmis.apmismobile.data.database.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;

import java.util.Date;

@Entity(tableName = "wallet", indices = {@Index("refCode"), @Index(value = "refCode")})
public class Wallet {

    private double balance;
    private double ledgerBalance;

       /* balance: { type: Schema.Types.Number, default:0 },
        ledgerBalance: { type: Schema.Types.Number, default:0 },
        transactions: [{
        transactionType: { type: String, default: 'Cr' },
        amount: { type: Schema.Types.Number, default: 0 },
        transactionMedium: { type: String, default: 'Cash' },
        transactionStatus: { type: String, default: 'InComplete' },
        refCode: { type: String, required: false },
        destinationId: { type: String, required: false },
        destinationType: { type: String, required: false },
        sourceId: { type: String, required: false },
        sourceType: { type: String, required: false },
        paidBy: { type: String, required: false },
        balance: { type: Schema.Types.Number, default: 0 },
        ledgerBalance: { type: Schema.Types.Number, default: 0 },
        description: { type: String, required: false },
        createdAt: { type: Date, 'default': Date.now },
        updatedAt: { type: Date, 'default': Date.now }
    }],
        description: { type: String, required: false },
        createdAt: { type: Date, 'default': Date.now },
        updatedAt: { type: Date, 'default': Date.now }
    */
}
