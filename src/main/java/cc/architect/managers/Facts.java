package cc.architect.managers;

import cc.architect.Architect;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Facts {
    public static final String[] FACTS = {
        // facts
        "eif5GXYBcwkJK3P",
        "Y2n3yM3Mi5Dyagc",
        "vhpXgbQKTMeQ5w4",
        "3JtA3527BVZbesM",
        "1IpI56QCdApGD33",
        "mm22LuT8w4c8hjl",
        "I3O438Bqx90imFk",
        "bYXFPC4qNA5Ox6q",
        "rtcDFAPy90vcec8",
        "wK5dBo4vc1DkOrc",
        "WhhEyFKTw9p7x7U",
        "s5EHHx8cm2Z3bkg",
        "bDHv8yOKA68Q2Jp",
        "FGbdjVbU3mfsVTy",
        "J3C5JYIYV59frZU",
        "gT3nfCg6DzKCC6A",
        "TQaypbHWwxb1itF",
        "PXeZTLsQK6JxIjt",
        "sYMM9WznOgSZvpu",
        "TbVp04F8kil339i",
        "HgaTEUmhxwxsnyD",
        "4RN2gBtx3o27C19",
        "ozZgLLeuOJ0rQoW",
        "mfAnIq5uMVAPxah",
        "2CSok6RUNJl0O6a",
        "7kVNSYo2yDozYt5",
        "bzNKoXFacK6gWE9",
        "1GpDQrLqhNHIk4n",
        // random emeralds
        "AVU9lNoFkNdiI1x",
        "JUAiNM7Drel6lSY",
        // score
        "FntiFqnZGccgWMc",
        "aoWUqAgUPD7Cmif",
        "OVy3wM5ZaTHvNhS",
        "zwcqLtem6MxpNw4",
        "bH3UIgC7Hkn8BkL",
        "FTWE99fmU4giNun",
        "lDRUX374hA4Eyap",
        "ZDt9elP1DPlJADZ",
        "hqPGvyePqZJ51DX",
        "upG7McPVJU2WVMb",
        "KUXYfZADmHCOrQe",
        "nZkpLDZMxAZPZHH",
        "N5l6x7JMXsPeZtp",
        "Cwb87OtF0LeSVcu",
        "RBcHA2i36UD2bIk",
        "e5xK7T5c5qZcZGL",
        "qqbbzizdeI4voWn",
        "XjViFQNw3CWcxyI",
        "oGqbMguoW9JD8of",
        "EeLHXQpXLvBZbW1",
        "1qivP4AxFljdUIj"
    };
    public static void synchronize(Player p) {
        // synchronize facts
        for (String fact : FACTS) {
            // get fact data
            String data = Meta.get(p, Meta.FACT + "_" + fact);
            // set fact data
            Bukkit.dispatchCommand(Architect.CONSOLE, "tw facts set " + fact + " " + (data == null ? "0" : data) + " " + p.getName());
        }
    }
}
