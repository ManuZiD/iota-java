package org.iota.jota;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.core.IsNull;
import org.iota.jota.config.types.FileConfig;
import org.iota.jota.dto.response.GetAttachToTangleResponse;
import org.iota.jota.dto.response.SendTransferResponse;
import org.iota.jota.error.ArgumentException;
import org.iota.jota.model.Transaction;
import org.iota.jota.model.Transfer;
import org.iota.jota.pow.pearldiver.PearlDiverLocalPoW;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Created by pinpong on 01.10.17.
 */
public class IotaLocalPoWTest {

    private static final String TEST_SEED1 = "IHDEENZYITYVYSPKAURUZAQKGVJEREFDJMYTANNXXGPZ9GJWTEOJJ9IPMXOGZNQLSNMFDSQOTZAEETUEA";
    private static final String TEST_ADDRESS_WITH_CHECKSUM_SECURITY_LEVEL_2 = "LXQHWNY9CQOHPNMKFJFIJHGEPAENAOVFRDIBF99PPHDTWJDCGHLYETXT9NPUVSNKT9XDTDYNJKJCPQMZCCOZVXMTXC";
    private static final String TEST_MESSAGE = "JUSTANOTHERJOTATEST";
    private static final String TEST_TAG = "JOTASPAM9999999999999999999";
    private static final int MIN_WEIGHT_MAGNITUDE = 14;
    private static final int DEPTH = 4;
    
    private static final String[] TEST_TRYTES_VALUE_TRANSFER = new String[] {
            "999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999NNIWDJKXYXOYEHXQQBOPWSYYKUVNBLBJFRBCPZBTNOG9NJHLSOCANRRGACYCIOYQJATHYUIPOFXLV9URBUIA999999999999999999999999IOTA9ACCOUNTS9TRANSFER999999TQWPAD99C99999999C99999999RTAVSEEHIJEYJYHYPFHTLVQLGGHBBTBWJEWBUCKXWHNBEA9YIIKQOCDIKBLJOPUOKLBCGFDJX9CCQPRIWEQVFKIGQPKTDADIAHNVCCV9OCIAOSTQVDQVFY9YJRITGJJMRGVJPPWIEIPJRZTMTBNWPLMRFPUAHPY999EQVFKIGQPKTDADIAHNVCCV9OCIAOSTQVDQVFY9YJRITGJJMRGVJPPWIEIPJRZTMTBNWPLMRFPUAHPY999IOTA9ACCOUNTS9TRANSFER99999FMBUPTZNF999999999MMMMMMMMMCFQLIPUFO9APITWHAQECZUQ9ONB",
            "YWGMDTHWBQBUYCLHRKSDLKEUYOLLGRUWOUDGHZBYLR9E9OOVOEEPGUAAVVWONYESBLZIYSQE9Y9BKY9IDHLOKRGKMWLULBV9YIEUXQDXOMYCBBZWPUXPRFORAXZDPYNRDVU9THLKNZKMBISYYCVRSCYBUOWLCXYTEX9AWFVLPTHMKXGLZGLWMSDJBKJYBFV9AYWYFADKOVVLYVV9UYRDGNCLFHUAGDOZHMRGABFDWJFGYLRCVJCARLADDRVJFTRUXI9ZIMYJRGV9WEHFPABRZSI9GPBHBEKVFXZOXZCLGXFHA9QHRAVLUZDZQBIAVKPNJTCCBZY9ERFTYGEQSNQVAYSQDMGTHLMZEGWOGXYTUCNUWCJOMVX9ZEJE9SO9BNUGBHLOJZPQBPPPARJUFIMF9NNOKURSTEWRFFKDZOH9PZEYJXYXWXZJVRAGCJSNNDGVXG9QJVEPC9BCDRWTNTZYVWMKTJHMYVQJNULSPWANGCUGBFDWZNNXWYNFLNZWLCHSDAUDYVRH9AAIIZZJPEYEPPFYZQGLUTAADGZDHPWZCHREYRHKVIBDNDBQBT9NEBAT9SRPGBKGQBVKNGFYHKNXGDFYPPT9LUYGREAPGCDZZCDCZQUU9KASZSUFLHYQIQRBLQ9PB9KCZVEXHB9MOAWILVHRJEQONOKRVGQPUDEAJPBWBZDRBHKVWODTVZZYGQMEZQFBIDLCDZIJT9HHENWWPTCTWIIKHVMLSHXAFMGKBBLPZXACENGMFLIRSAFLTSHIFSMSUNJSIGICDHGOWMT9PGDWAPDPTBOXEXQKGYFCPXKXIAJAZTXLR9VBLVMTFLVTBQYLQRUAJNLZZZIGDMQFGW9QSTIZCJCZUFIEXXMPINNJCEMGFWETKBBGBWXRXPCKJOCQMUPFAJWHVXETDGHCUONFFLCXNZZGRVJGUUDSZCRFBAGUOGREXQVWIXYQFRWCWUXENLKUIEGYMXYFPXXHGASEOGDNLJMENIWLQNXLGROXHNMVQKCTQSPPRLICRJ9NQR9RXINSBKLYYNIHENVFXGHAE9NFCJOXZZHLNIMZKMKIXQMSYXRYNME9IJRQOVSEWQRASU9TZBHNRXKTLE9GNUWSPCHXNRSPTLDYYKYDORH9FDRYRVLUWQEVUKHPKUEKZOOHBOS9APGMMMXFPPNOIMTSLPKBMOMT9SDRLBCUXNBQMTHFGYPWGRPXEWFILFWRSBKEBNXHFYPW9ZSHURXAEBAIKYNHXBZT9ZQCBCKWONKZEQSKIAOCXDUJAGGKINLYCNUVWBDJTCRSUOQBUSHDWFXBPYJQWSANITQOUWXIHTRXJZ9RASQXZQEQXQWQUOOMMCMWRAIKUVCUEQLPPUNBAUN9K9RUUXUTDURPDKCBQJHNRKBGUAPMUFSCUUZNBVOKOMQV9XNGHAMP9JN9MKSFZRUCCKPMLY9IME9BAZPFRHTUWLYE99YWTCALYZXQ9RSJGSSTHCSNQQLHENBJRCOVCRPANFVLOKOY9LOMPMXKR9PQS9UYXEALDZUHZ9RQSTXSXT9IKVLHYV9PDNXZMN9CNWCBYEUWTSPNNRMFULNPY9SDBLJBSRZGWCT9ZFEANEWGDADSMEBRSBZLPEGDLSHYEKBALWYGWQNJZOSXGNK9BPNBRUB9DQDJNCGWKAIZJOF9PTEGSULBFC9WELZAAGXUBFEPRWELFIM9SAZMBZQBBZXSVXXRU9VYEXI9QLCWEDMJYIOCJTCGFIYOUIOLSKPVFXUZQDKRBVQQPRGPV9JWYFMOMRXIVEVUTWI9ZKVPRWTWSLAFKTQQCWVOUYEHEXTPQZPKHXRGNFCXDOZHDDQOVWLYFNKERVGNZNOETBUGIDXCIHUBLUWDOJVKJXXQPDNPLLXHYDZMACKBRVOORHRJYYAHKFSCXXPRHLRUKCXXRYI9HSZSEPCQDWGYP9BYZXWPOAUXLNCERGYUACGALDJLCGHQZKLUBXRHBDBUFZLSXACIAWGBPNINLQET9AFMXKTLRZAQRWXRYZRWAKZHHBZJCOXLSYDVORZQPWUSJUHI9VP9BSNLYXKQQLUKPHKHOYARSIREBW9IGNBEXZXSXRHHPIRTAAULXDJX9UPTO9DXUUDNUBHCWSQYEZBYMQHCARUVIAOKAWIFDYYYL9EQZZWJGJWJPKOTWFMUGZDEMGFVOHHKVAGQPOWIL9DIQEIPVJL9DEUVKEMKRJDYDVACBRXNYSIHEV9HWQECNHHZBKWDVM9MY9IGBSEQSMSRMZNRZXHAGBKMRFCA999999999999999999999999999IOTA9ACCOUNTS9TRANSFER999999TQWPAD99B99999999C99999999RTAVSEEHIJEYJYHYPFHTLVQLGGHBBTBWJEWBUCKXWHNBEA9YIIKQOCDIKBLJOPUOKLBCGFDJX9CCQPRIWDAG9LCCSHVDLUDX9UTZIUAOA9BNAYOFAVKUKKWDJDZODLUK9NDMGCZBA9REFPMQSOMDFVWVMEIHU99999EQVFKIGQPKTDADIAHNVCCV9OCIAOSTQVDQVFY9YJRITGJJMRGVJPPWIEIPJRZTMTBNWPLMRFPUAHPY999IOTA9ACCOUNTS9TRANSFER99999NKCXPTZNF999999999MMMMMMMMMNMKXOMG9LYENZYYOXP9CAQKKPQG",
            "RASZHIFYMGATRQXGMSJQWQHR9RGQMYJZGRRUMIP9PEJHWLJIFO9OLVVMVKZXSHUKXMQFROOKOEJJBKBWCCYIGCTSEESZFSFGRE9SFSDQJK9CBYWUCRIJTNGD9GBXYRZOIZADTRJEV9CTKFVLCKXEXYNILDBMWPCWMAOBLQAPITBCIKIVKJGAVPPTWJSWGDDIZZVKJVVWIHYDTZDPQN9KWLJIOUP9LNODIFQLINSYCPCGSS9KWXDYM9POIQSJOFRUDVPYDSKZVJIS9ZUGSRW9WMV9YVACFQAZ9XZTPDADMFETEAOTGVUJLEAGQZBQVASQZQOCUCKOZPSIISXYXUIIPNPXEOWPBSTENOEYZYHOQJQHCTWMWJIFFDZMSTIEYMBULUSBITXQFURTPTYQXOD9WNTNJRQIZGLZXGEFATGADUQF9XMPCY9QBX9RKDFJON9OMJVYDAWKWUHNMCSCDSYZVFDUAERISVTZVLQBO9JTZWOQHZYNNBBBIMCWKMSDSPDXDJDQCCHOX9MYTTJVWMJSWMJLSWOZGZSTAQXAKKFYMGXJPNECEMVEWRWOCY9LMSLRFOIHSIGFB9CZNOUMYOSABKBO9TVZUXGSLOWJM9GYI9ONSKFFOEMGDVYJXPHUDRJHYZYMKPOARWDEHCWJBWZY9LDKWYLYSTHGBFRNLTZVRMILKVD9GQE9FIBAGD9ECTHGCSRJ9PJWNWQQDBPGWRARRXQM9GWQPMERSBRUYHA9ZFNTQZOCYTYNEBO9YWNVQMIQDSLFCIJA9DMDBPLWZEKTIYFRXHXS9LMRAHKKOIJPFCNLFX9GCY9SWUFFAARADXHWIFLLKFAFUZLCMGVHAKPHWGEO9WMNNTUQRITWPBLJUCGTICCYUZJDNW9UUEWHKFBKCDNVVSBJCLNKZVSJXKYUQCDTHASLWRIHBUD9NCHOVYBTFJBHGBLIQRJQL9XQBNMXPEBIWUHDMPAXERPVRDKMCXXWHPPNKELOPBBVRNIPTSNIXJGPYDM9X9BSNNYIPVDAEONMLIUQRVWZPGS9NKOQPHEIGNFD9ZIWBSEQJIRBPSMQRTAXSJDBFQYHVKYKNLCRBJOR9LVBQOKXCHIQF9VQIOCDJEJUB9QQASHOIATPSLKTDATJZHKTQMYDLKTPDXOOXNRXXWUSJGOSSSEFDGRTNCDOSWFVFDULFCMAMYBKOEWKSLQTASADLVGHPUWFJRDONSVUAWPKCXMGYQP9BBTABENPCOC9EMAOBRSJVWLKMRHBJSZBANINRUTZINISDX9AIHIETRIDRN9XXVQWYKCQZ9ZOGVHGHURNPPAKPGMSGJKETOV99WKXIIYMJOZOVVVDBXFCUGTSXXEIGCNEVHFWBJQSQUIOXXDOAMDXEKZNWHAVZJYTMZHSSUSMLCECSYSRVWSCYHKORQHMRDZBBUNQKRYPEBMGPGBGBUUVTLZONUOZ9PWEAZNVTVLELGUVPX9FOFDKFZOCBE9RUHJSQGLSWZMWLQDHWBMRRDDJPIZBOEDPJONMC9UHWIOZYBJXZQJHTZCOORPWDFRWTOQAQDAFLCSUABRGAWFRJWKSSJJDWJIF9GNPQFVJOD9ZELCUXUUFBWXGFFIZ9RYRTNBCWQAASJAUMQIYKNPQPWCYYMKOZZLFHCUCVMJSA9QRMYFGXKHVJYLWNZDSQUNOUHTXWT9ETGSZ9ZTNPRBDURNTCBSATXOGGIJKCPCISBPZHFPNWBZEBOUVIDIXOKCZPTRFYPBSUJGWGJBINNWNPRSIIRSQISDR9HTHYOHVJBXFBTQLZXMPHE9KRXRMF9HSEMZCLIP9EIVWLLEFGJUNWGKQJMEDYXYZQD9NO9ICFQTDJUPCTP9BIPLYEJYHNYBLBWRFRX9GOGPQCYFEUKVNTYOH9QWDNPMQMVZRM9ULLBXMKSHNOLYRIFYLKCKPTMPIUUJWUVFGUKNZGZDWOFIZJH9OT9KSLJSZKWVYYYYHW9EPFRXOTEWNPAMOKFBLYDYXOV9R9RUEMDELTRXFTVNVZNBRJAPQWSOB9NXERDJYA99VLUKVS9VTQCRB9XYCYMWRSJQCGRW9WUKSUNKWZLNMCZNFTAAHML999WMZESFYED9AKDHRONUCKXUFWGDSBATTJCEEOKPCWIVVQQTTDALUXCLNXGSMGBBZZFJONZQIGLA9PYREAFNGGPFWHKGNWWDIQEIPVJL9DEUVKEMKRJDYDVACBRXNYSIHEV9HWQECNHHZBKWDVM9MY9IGBSEQSMSRMZNRZXHAGBKMRFCAERZ999999999999999999999999IOTA9ACCOUNTS9TRANSFER999999TQWPAD99A99999999C99999999RTAVSEEHIJEYJYHYPFHTLVQLGGHBBTBWJEWBUCKXWHNBEA9YIIKQOCDIKBLJOPUOKLBCGFDJX9CCQPRIWIJEXBNHMIBEWDCDFPGXRUDJGXDR9NXGEFHJJEOZPBZE9GSNYHDFBLTT9VMMFWFPFWYDBMBLNCSREZ9999EQVFKIGQPKTDADIAHNVCCV9OCIAOSTQVDQVFY9YJRITGJJMRGVJPPWIEIPJRZTMTBNWPLMRFPUAHPY999IOTA9ACCOUNTS9TRANSFER99999CTX9PTZNF999999999MMMMMMMMMIBYBANZPHARBAWTVQHJADRXLVZY",
            "RBTCFDTCEAXCGDEAMDCDIDFDEAXCCDHDPCFA999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999EWLWIKTWEIUBCMECMFHYVSLMMR9XLLABPFY99GSXAKLOAATIQQHSI9MY9TFWZBLB9EBQUGFOSCLC9MXBZA99999999999999999999999999GNUA9ACCOUNTS9TRANSFER999999TQWPAD99999999999C99999999RTAVSEEHIJEYJYHYPFHTLVQLGGHBBTBWJEWBUCKXWHNBEA9YIIKQOCDIKBLJOPUOKLBCGFDJX9CCQPRIWHBKOBLI9TAKAMIZOYISMAJVGKRVJNDCAM9EWGTWBBDHSJMRIBSSYS9DTYVBEOBPBDSFULPBJHLBOA9999EQVFKIGQPKTDADIAHNVCCV9OCIAOSTQVDQVFY9YJRITGJJMRGVJPPWIEIPJRZTMTBNWPLMRFPUAHPY999IOTA9ACCOUNTS9TRANSFER99999CP9BPTZNF999999999MMMMMMMMMGFFGWAGRRSN9CMWRXRLFFTZFBJG"
    };

    private IotaAPI iotaClient;

    @BeforeEach
    public void createApiClientInstance() throws Exception {
    iotaClient = new IotaAPI.Builder().config(new FileConfig()).localPoW(new PearlDiverLocalPoW()).build();
    }

    @Test
    public void shouldSendTransfer() throws ArgumentException {
        List<Transfer> transfers = new ArrayList<>();
        transfers.add(new Transfer(TEST_ADDRESS_WITH_CHECKSUM_SECURITY_LEVEL_2, 0, TEST_MESSAGE, TEST_TAG));
        SendTransferResponse str = iotaClient.sendTransfer(TEST_SEED1, 2, DEPTH, MIN_WEIGHT_MAGNITUDE, transfers, null, null, false, false, null);
        assertThat(str.getSuccessfully(), IsNull.notNullValue());
    }

    @Disabled("{\"error\":\"Wrong MinWeightMagnitude. requested: 11, expected: 9\"}")
    @Test
    public void shouldHaveEqualOrderTrytes() throws Exception {
        GetAttachToTangleResponse localResponse = iotaClient.attachToTangle(
                TEST_SEED1, TEST_SEED1, 
                11, TEST_TRYTES_VALUE_TRANSFER);
        
        iotaClient = new IotaAPI.Builder().config(new FileConfig()).build();
        GetAttachToTangleResponse remoteResponse = iotaClient.attachToTangle(
                TEST_SEED1, TEST_SEED1, 
                11, TEST_TRYTES_VALUE_TRANSFER);
    
        for (int i = 0; i < TEST_TRYTES_VALUE_TRANSFER.length; i++) {
            assertEquals(new Transaction.Builder().buildWithTrytes(localResponse.getTrytes()[i]).getValue(),
                    new Transaction.Builder().buildWithTrytes(remoteResponse.getTrytes()[i]).getValue());
        }
    }
}
