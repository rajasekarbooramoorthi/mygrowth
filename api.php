<?php
header("Content-Type: application/json");

// Allow POST only
if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    echo json_encode(["status" => false, "message" => "POST only"]);
    exit;
}

// Database connection
$host = "localhost";
$user = "rajasekar";
$pass = 'RajDroid@#24811';
$db   = "my_growth";

$conn = mysqli_connect($host, $user, $pass, $db);
if (!$conn) {
    echo json_encode(["status" => false, "message" => "DB connection failed"]);
    exit;
}

$request = json_decode(file_get_contents("php://input"), true);
$action = $request["action"];


if ($action === "insert_daily_task") {


    $dt_name   = $request['taskName']      ?? "";
    $dt_due    = $request['dueDate']  ?? "";
    $dt_priority    = $request['priority']  ?? "";


    // Traditional query
    $sql = "INSERT INTO Daily_Task (dt_name, dt_date_created, dt_due_date)
            VALUES ('$dt_name', CURDATE(), '$dt_due')";

    $result = mysqli_query($conn, $sql);

    echo json_encode(["status" => $result]);
    exit;
}


/* ============================================================
   2. SELECT DAILY TASK (GROUPED RESPONSE) - OPTIMIZED
   ============================================================ */
if ($action === "get_daily_task") {

    // Date references
    $today      = date("Y-m-d");
    $tomorrow   = date("Y-m-d", strtotime("+1 day"));

    $weekStart  = date("Y-m-d", strtotime("monday this week"));
    $weekEnd    = date("Y-m-d", strtotime("sunday this week"));

    $nextWeekStart  = date("Y-m-d", strtotime("monday next week"));
    $nextWeekEnd    = date("Y-m-d", strtotime("sunday next week"));

    $monthStart     = date("Y-m-01");
    $monthEnd       = date("Y-m-t");

    $nextMonthStart = date("Y-m-d", strtotime("first day of next month"));
    $nextMonthEnd   = date("Y-m-d", strtotime("last day of next month"));

    // All group keys
    $categories = [
        "overDue", "today", "tomorrow",
        "this_week", "next_week",
        "this_month", "next_month",
        "future"
    ];

    // Pre-create empty category groups
    $groups = [];
    foreach ($categories as $cat) {
        $groups[$cat] = [
            "taskName" => $cat,
            "list"     => []
        ];
    }

    // Helper function to determine category
    function getCategory($due, $today, $tomorrow,
                         $weekStart, $weekEnd,
                         $nextWeekStart, $nextWeekEnd,
                         $monthStart, $monthEnd,
                         $nextMonthStart, $nextMonthEnd) {

        if ($due < $today) return "overDue";
        if ($due == $today) return "today";
        if ($due == $tomorrow) return "tomorrow";
        if ($due >= $weekStart && $due <= $weekEnd) return "this_week";
        if ($due >= $nextWeekStart && $due <= $nextWeekEnd) return "next_week";
        if ($due >= $monthStart && $due <= $monthEnd) return "this_month";
        if ($due >= $nextMonthStart && $due <= $nextMonthEnd) return "next_month";
        return "future";
    }

    // Fetch tasks
    $q = mysqli_query($conn, "SELECT * FROM Daily_Task ORDER BY dt_due_date ASC");

    while ($row = mysqli_fetch_assoc($q)) {
        $due = $row["dt_due_date"];

        $category = getCategory(
            $due, $today, $tomorrow,
            $weekStart, $weekEnd,
            $nextWeekStart, $nextWeekEnd,
            $monthStart, $monthEnd,
            $nextMonthStart, $nextMonthEnd
        );

        // Add into category
        $groups[$category]["list"][] = $row;
    }

    // Output as array (needed for frontend)
    echo json_encode([
        "status" => true,
        "data"   => array_values($groups)
    ]);
    exit;
}


/* ============================================================
   3. INSERT MASTER INTERVIEW
   ============================================================ */
if ($action === "insert_master_interview") {

    $name = $_POST['ami_name'] ?? "";
    $status = $_POST['ami_status'] ?? "";
    $due = $_POST['ami_due_date'] ?? "";

    $sql = "INSERT INTO Android_Master_interview(ami_name, ami_status, ami_date_created, ami_due_date)
            VALUES(?, ?, CURDATE(), ?)";

    $stmt = mysqli_prepare($conn, $sql);
    mysqli_stmt_bind_param($stmt, "sis", $name, $status, $due);

    echo json_encode(["status" => mysqli_stmt_execute($stmt)]);
    exit;
}

/* ============================================================
   INSERT MULTIPLE INTERVIEW CONCEPTS WITH MULTIPLE LINKS
   ============================================================ */
if ($action === "insert_interview_concept") {

    $conceptList = $request["conceptModel"]; // <-- array of ConceptModel objects
    $status = false;

    foreach ($conceptList as $concept) {

        $conceptId   = mysqli_real_escape_string($conn, $concept["conceptId"]);
        $conceptName = mysqli_real_escape_string($conn, $concept["conceptName"]);
        $links       = $concept["links"]; // array of strings

        // 1️⃣ Insert into Main Concept Table
        $insertConcept = mysqli_query($conn,
            "INSERT INTO Android_Interview_Prepration ( aip_name)
             VALUES ('$conceptName')"
        );

        // If main insert failed → skip remaining
        if (!$insertConcept) continue;

        // 2️⃣ Insert all links into link table
        foreach ($links as $link) {
            $link = mysqli_real_escape_string($conn, $link);
            mysqli_query($conn,
                "INSERT INTO Android_Prepration_Link (apl_id, apl_link)
                 VALUES ('$conceptId', '$link')"
            );
        }

        $status = true;
    }

    echo json_encode([
        "status"  => $status,
        "message" => $status ? "All inserted successfully" : "Failed"
    ]);
    exit;
}



/* ============================================================
   4. SELECT MASTER PASSWORD
   ============================================================ */
if ($action === "get_master_password") {

    $data = [];
    $q = mysqli_query($conn, "select psw_sno,psw_name,psw_password,psw_date_updated from psw_manager");

    while ($row = mysqli_fetch_assoc($q)) {
        $data[] = $row;
    }

    echo json_encode([
        "status" => count($data) > 0,
        "data"   => $data
    ]);
    exit;
}



/* ============================================================
   4. SELECT MASTER SKILLS
   ============================================================ */
if ($action === "get_master_skills") {

    $data = [];
    $q = mysqli_query($conn, "select ms_sno,ms_id,ms_name,ms_tag,ms_folder,ms_type from MASTER_SKILL where ms_flag='0'");

    while ($row = mysqli_fetch_assoc($q)) {
        $data[] = $row;
    }

    echo json_encode([
        "status" => count($data) > 0,
        "data"   => $data
    ]);
    exit;
}


/* ============================================================
   4. SELECT SELF IMPROVEMENT
   ============================================================ */
if ($action === "get_self_improvement") {

    $data = [];
    $q = mysqli_query($conn, "select ai_sno,ai_name,ai_description,ai_link from  selfImprovement");

    while ($row = mysqli_fetch_assoc($q)) {
        $data[] = $row;
    }

    echo json_encode([
        "status" => count($data) > 0,
        "data"   => $data
    ]);
    exit;
}



/* ============================================================
   4. SELECT MASTER BANK DETAILS
   ============================================================ */
if ($action === "get_master_Bank") {

    $data = [];
    $q = mysqli_query($conn, "select b_sno,b_name,b_cust_id,b_acc_number,b_card_num,b_cpin,b_psw,b_profile_password,b_updated_date from Bnk_details");

    while ($row = mysqli_fetch_assoc($q)) {
        $data[] = $row;
    }

    echo json_encode([
        "status" => count($data) > 0,
        "data"   => $data
    ]);
    exit;
}

/* ============================================================
   5. INSERT INTERVIEW PREPRATION
   ============================================================ */
if ($action === "insert_interview_prep") {

    $id   = $_POST['aip_id'] ?? "";
    $name = $_POST['aip_name'] ?? "";
    $link = $_POST['aip_link'] ?? "";
    $status = $_POST['aip_status'] ?? "";

    $sql = "INSERT INTO Android_Interview_Prepration(aip_id, aip_name, aip_link, aip_status, aip_date_created)
            VALUES(?, ?, ?, ?, CURDATE())";

    $stmt = mysqli_prepare($conn, $sql);
    mysqli_stmt_bind_param($stmt, "issi", $id, $name, $link, $status);

    echo json_encode(["status" => mysqli_stmt_execute($stmt)]);
    exit;
}


/* ============================================================
   6. SELECT INTERVIEW PREPRATION
   ============================================================ */
if ($action === "get_interview_prep") {

    $data = [];
    $q = mysqli_query($conn, "SELECT * FROM Android_Interview_Prepration ORDER BY aip_sno DESC");

    while ($row = mysqli_fetch_assoc($q)) $data[] = $row;

    echo json_encode([
        "status" => count($data) > 0,
        "data"   => $data
    ]);
    exit;
}


/* ============================================================
   7. INSERT PREPRATION LINK
   ============================================================ */
if ($action === "insert_prep_link") {

    $id   = $_POST['apl_id'] ?? "";
    $link = $_POST['apl_link'] ?? "";
    $status = $_POST['apl_status'] ?? "";

    $sql = "INSERT INTO Android_Prepration_Link(apl_id, apl_link, apl_status, apl_date_created)
            VALUES(?, ?, ?, CURDATE())";

    $stmt = mysqli_prepare($conn, $sql);
    mysqli_stmt_bind_param($stmt, "isi", $id, $link, $status);

    echo json_encode(["status" => mysqli_stmt_execute($stmt)]);
    exit;
}


/* ============================================================
   9. INSERT PASSWORD MANAGER
   ============================================================ */
if ($action === "insert_psw") {

    $name = $_POST['psw_name'] ?? "";
    $pass = $_POST['psw_password'] ?? "";

    $sql = "INSERT INTO psw_manager(psw_name, psw_password, psw_date_created)
            VALUES(?, ?, CURDATE())";

    $stmt = mysqli_prepare($conn, $sql);
    mysqli_stmt_bind_param($stmt, "ss", $name, $pass);

    echo json_encode(["status" => mysqli_stmt_execute($stmt)]);
    exit;
}


/* ============================================================
   10. SELECT PASSWORD MANAGER
   ============================================================ */
if ($action === "get_psw") {

    $data = [];
    $q = mysqli_query($conn, "SELECT * FROM psw_manager ORDER BY psw_sno DESC");

    while ($row = mysqli_fetch_assoc($q)) $data[] = $row;

    echo json_encode([
        "status" => count($data) > 0,
        "data"   => $data
    ]);
    exit;
}


if ($action === "get_books_summary") {
    $data = [];

    $query = "
        SELECT
            bsd.bsd_sno,
            bsd.bsd_name,
            GROUP_CONCAT(bsdl.bsdl_link SEPARATOR '|||') AS links
        FROM BookSummaryDevelopment bsd
        LEFT JOIN BookSummaryDevelopmentLink bsdl
            ON bsdl.bsdl_id = bsd.bsd_id
        GROUP BY bsd.bsd_sno, bsd.bsd_name
        ORDER BY bsd.bsd_sno ASC
    ";

    $result = mysqli_query($conn, $query);

    while ($row = mysqli_fetch_assoc($result)) {
        $data[] = [
            "id"   => $row["bsd_sno"],
            "name" => $row["bsd_name"],
            "links"     => !empty($row["links"]) ? explode('|||', $row["links"]) : []
        ];
    }

    echo json_encode([
        "status" => !empty($data),
        "data"   => $data
    ], JSON_UNESCAPED_SLASHES);

    exit;
}


if ($action === "get_books_career") {
    $data = [];

    $query = "
        SELECT
            cb.cb_id,
            cb.cb_name,
            GROUP_CONCAT(cbl.cbl_link SEPARATOR '|||') AS links
        FROM CareerBooks cb
        LEFT JOIN CareerBooksLink cbl
            ON cbl.cbl_id = cb.cb_id
        GROUP BY cb.cb_id, cb.cb_name
        ORDER BY cb.cb_id ASC
    ";

    $result = mysqli_query($conn, $query);

    while ($row = mysqli_fetch_assoc($result)) {
        $data[] = [
            "id"    => $row["cb_id"],
            "name"  => $row["cb_name"],
            "links" => !empty($row["links"]) ? explode('|||', $row["links"]) : []
        ];
    }

    echo json_encode([
        "status" => !empty($data),
        "data"   => $data
    ], JSON_UNESCAPED_SLASHES);

    exit;
}

if ($action === "get_data_structure_notes") {
    $data = [];

    $query = "
        SELECT
            ds.ds_id,
            ds.ds_name,
            GROUP_CONCAT(dsl.dsl_link SEPARATOR '|||') AS links
        FROM DataStructure ds
        LEFT JOIN DataStructureLink dsl
            ON dsl.dsl_id = ds.ds_id
        GROUP BY ds.ds_id, ds.ds_name
        ORDER BY ds.ds_id ASC
    ";

    $result = mysqli_query($conn, $query);

    while ($row = mysqli_fetch_assoc($result)) {
        $data[] = [
            "id"    => $row["ds_id"],
            "name"  => $row["ds_name"],
            "links" => !empty($row["links"]) ? explode('|||', $row["links"]) : []
        ];
    }

    echo json_encode([
        "status" => !empty($data),
        "data"   => $data
    ], JSON_UNESCAPED_SLASHES);

    exit;
}

if ($action === "get_development_growth") {
    $data = [];

    $query = "
        SELECT
            d.dp_id,
            d.dp_name,
            GROUP_CONCAT(dl.dpl_link SEPARATOR '|||') AS links
        FROM Development d
        LEFT JOIN DevelopmentLink dl
            ON dl.dpl_id = d.dp_id
        GROUP BY d.dp_id, d.dp_name
        ORDER BY d.dp_id ASC
    ";

    $result = mysqli_query($conn, $query);

    while ($row = mysqli_fetch_assoc($result)) {
        $data[] = [
            "id"    => $row["dp_id"],
            "name"  => $row["dp_name"],
            "links" => !empty($row["links"]) ? explode('|||', $row["links"]) : []
        ];
    }

    echo json_encode([
        "status" => !empty($data),
        "data"   => $data
    ], JSON_UNESCAPED_SLASHES);

    exit;
}


if ($action === "get_english_communication") {
    $data = [];

    $query = "
        SELECT
            ec.ec_sno,
            ec.ec_name,
            GROUP_CONCAT(ecl.ecl_link SEPARATOR '|||') AS links
        FROM EnglishCommunication ec
        LEFT JOIN EnglishCommunicationLink ecl
            ON ecl.ecl_id = ec.ec_id
        GROUP BY ec.ec_sno, ec.ec_name
        ORDER BY ec.ec_sno ASC
    ";

    $result = mysqli_query($conn, $query);

    while ($row = mysqli_fetch_assoc($result)) {
        $data[] = [
            "id"    => $row["ec_sno"],
            "name"  => $row["ec_name"],
            "links" => !empty($row["links"]) ? explode('|||', $row["links"]) : []
        ];
    }

    echo json_encode([
        "status" => !empty($data),
        "data"   => $data
    ], JSON_UNESCAPED_SLASHES);

    exit;
}


if ($action === "get_interview_tips") {
    $data = [];

    $query = "
        SELECT
            ip.ip_sno,
            ip.ip_name,
            GROUP_CONCAT(il.ipl_link SEPARATOR '|||') AS links
        FROM InterviewTips ip
        LEFT JOIN InterviewTipsLink il
            ON il.ipl_id = ip.ip_id
        GROUP BY ip.ip_sno, ip.ip_name
        ORDER BY ip.ip_sno ASC
    ";

    $result = mysqli_query($conn, $query);

    while ($row = mysqli_fetch_assoc($result)) {
        $data[] = [
            "id"    => $row["ip_sno"],
            "name"  => $row["ip_name"],
            "links" => !empty($row["links"]) ? explode('|||', $row["links"]) : []
        ];
    }

    echo json_encode([
        "status" => !empty($data),
        "data"   => $data
    ], JSON_UNESCAPED_SLASHES);

    exit;
}


if ($action === "get_company_interview_tips") {
    $data = [];

    $query = "
        SELECT
            cip.cip_id,
            cip.cip_name,
            GROUP_CONCAT(cipl.cipl_link SEPARATOR '|||') AS links
        FROM CompanyInterviewTips cip
        LEFT JOIN CompanyInterviewTipsLink cipl
            ON cipl.cipl_id = cip.cip_id
        GROUP BY cip.cip_id, cip.cip_name
        ORDER BY cip.cip_id ASC
    ";

    $result = mysqli_query($conn, $query);

    while ($row = mysqli_fetch_assoc($result)) {
        $data[] = [
            "id"    => $row["cip_id"],
            "name"  => $row["cip_name"],
            "links" => !empty($row["links"]) ? explode('|||', $row["links"]) : []
        ];
    }

    echo json_encode([
        "status" => !empty($data),
        "data"   => $data
    ], JSON_UNESCAPED_SLASHES);

    exit;
}



/* ============================================================
   INSERT MULTIPLE INTERVIEW CONCEPTS WITH MULTIPLE LINKS
   ============================================================ */
if ($action === "insert_android_master_concept") {

    $conceptList = $request["conceptModel"]; // <-- array of ConceptModel objects
    $status = false;

    foreach ($conceptList as $concept) {

        $conceptId   = mysqli_real_escape_string($conn, $concept["conceptId"]);
        $conceptName = mysqli_real_escape_string($conn, $concept["conceptName"]);
        $links  = $concept["links"]; // array of strings

        // 1️⃣ Insert into Main Concept Table
        $insertConcept = mysqli_query($conn,
            "INSERT INTO AndroidMasterConcepts ( amc_name,amc_id)
             VALUES ('$conceptName','$conceptId')"
        );

        // If main insert failed → skip remaining
        if (!$insertConcept) continue;

        // 2️⃣ Insert all links into link table
        foreach ($links as $link) {
            $link = mysqli_real_escape_string($conn, $link);
            mysqli_query($conn,
                "INSERT INTO AndroidMasterConceptsLink (amcl_id, amcl_link)
                 VALUES ('$conceptId', '$link')"
            );
        }

        $status = true;
    }

    echo json_encode([
        "status"  => $status,
        "message" => $status ? "All inserted successfully" : "Failed"
    ]);
    exit;
}


if ($action === "get_android_master_concepts") {

    $data = [];



    $query = "
        SELECT
            amc.amc_id,
            amc.amc_name,
            GROUP_CONCAT(amcl.amcl_link SEPARATOR '|||') AS links
        FROM AndroidMasterConcepts amc
        LEFT JOIN AndroidMasterConceptsLink amcl
            ON amcl.amcl_id = amc.amc_id
        GROUP BY amc.amc_id, amc.amc_name
        ORDER BY amc.amc_id ASC
    ";

    $result = mysqli_query($conn, $query);

    while ($row = mysqli_fetch_assoc($result)) {
        $data[] = [
            "id"    => $row["amc_id"],
            "name"  => $row["amc_name"],
            "links" => !empty($row["links"]) ? explode("|||", $row["links"]) : []
        ];
    }

    echo json_encode([
        "status" => !empty($data),
        "data"   => $data
    ], JSON_UNESCAPED_SLASHES);

    exit;
}


if ($action === "insert_android_skill_specific") {

    $conceptList = $request["conceptModel"]; // <-- array of ConceptModel objects
    $status = false;

    foreach ($conceptList as $concept) {

        $conceptId   = mysqli_real_escape_string($conn, $concept["conceptId"]);
        $conceptName = mysqli_real_escape_string($conn, $concept["conceptName"]);
        $links  = $concept["links"]; // array of strings

        // 1️⃣ Insert into Main Concept Table
        $insertConcept = mysqli_query($conn,
            "INSERT INTO AndroidSkillSpecific ( ass_name,ass_id)
             VALUES ('$conceptName','$conceptId')"
        );

        // If main insert failed → skip remaining
        if (!$insertConcept) continue;

        // 2️⃣ Insert all links into link table
        foreach ($links as $link) {
            $link = mysqli_real_escape_string($conn, $link);
            mysqli_query($conn,
                "INSERT INTO AndroidSkillSpecificLink (assl_id, assl_link)
                 VALUES ('$conceptId', '$link')"
            );
        }

        $status = true;
    }

    echo json_encode([
        "status"  => $status,
        "message" => $status ? "All inserted successfully" : "Failed"
    ]);
    exit;
}



if ($action === "get_android_skill_specific") {

    $data = [];

    $query = "
        SELECT
            ass.ass_sno,
            ass.ass_id,
            ass.ass_name,
            GROUP_CONCAT(assl.assl_link SEPARATOR '|||') AS links
        FROM AndroidSkillSpecific ass
        LEFT JOIN AndroidSkillSpecificLink assl
            ON assl.assl_id = ass.ass_id   -- join condition
        GROUP BY ass.ass_sno, ass.ass_id, ass.ass_name
        ORDER BY ass.ass_sno ASC
    ";

    $result = mysqli_query($conn, $query);

    while ($row = mysqli_fetch_assoc($result)) {
        $data[] = [
            "id"    => $row["ass_id"],
            "name"  => $row["ass_name"],
            "links" => !empty($row["links"]) ? explode("|||", $row["links"]) : []
        ];
    }

    echo json_encode([
        "status" => !empty($data),
        "data"   => $data
    ], JSON_UNESCAPED_SLASHES);

    exit;
}


if ($action === "get_interview_preparation") {

    $data = [];

    // 1st Query — Fetch topics
    $query = "
        SELECT aip_id, aip_name
        FROM Android_Interview_Prepration
        ORDER BY aip_id ASC
    ";
    $result = mysqli_query($conn, $query);

    while ($row = mysqli_fetch_assoc($result)) {

        $id = $row["aip_id"];
        $name = $row["aip_name"];



        // 2nd Query — Fetch links for that topic
        $query2 = "
            SELECT apl_link
            FROM Android_Interview_Prepration_link
            WHERE apl_id ='$id'
        ";
        $result2 = mysqli_query($conn, $query2);

        $links = [];
        while ($row1 = mysqli_fetch_assoc($result2)) {
            $links[] = $row1["apl_link"];
        }

        $data[] = [
            "id"    => $id,
            "name"  => $name,
            "links" => $links
        ];
    }

    echo json_encode([
        "status" => !empty($data),
        "data"   => $data
    ], JSON_UNESCAPED_SLASHES);

    exit;
}



/* ============================================================
   INVALID ACTION
   ============================================================ */
echo json_encode(["status" => false, "message" => "Invalid action"]);
exit;
?>
