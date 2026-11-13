# Programming in Java – Assignment 3 (CO3)

**Student:** E. Lohitha  
**Register Number:** 192411103  
**Repository folder:** `CSA09-192411103-Assignment3`  
**Java used for local verification:** Java Platform Extension for Visual Studio Code (Oracle Corporation)

## Contents

- `Q1` – ATM simulation, exception hierarchy, multi-catch and finally
- `Q2` – User-defined checked exceptions, propagation and exception chaining
- `Q3` – File resource handling with manual close and try-with-resources
- `Q4` – Thread creation using `Thread` and `Runnable`, lifecycle experiments
- `Q5` – Cinema seat booking race condition and synchronization
- `Q6` – Wallet lock ordering and producer-consumer communication
- `Q7` – Fault-tolerant answer-script processing using an executor, parallel stream and one-thread-per-file comparison

Each question folder contains `Main.java`, `OUTPUT.txt` and a `Screenshots` folder.

## Compile and run

For each question, run the following commands from that question's folder:

```bash
javac Main.java
java Main
```

**Running Java Programs in Visual Studio Code (VS Code)**

1.Open Visual Studio Code.
2.Select File → Open Folder and open the extracted assignment folder.
3.Navigate to the required question folder (Q1, Q2, Q3, etc.).
4.Open that question's Main.java file.
5.Make sure the Extension Pack for Java is installed and a JDK is configured.
6.Open the integrated terminal using Terminal → New Terminal.
7.Navigate to the required question folder. For example:
cd Q1
8.Compile the Java program: javac Main.java
9.Run the program:
java Main
For Q1, enter the test input shown in the test list below when prompted by the program.
Verify that the program executes successfully and displays the expected output in the VS Code integrated terminal.
Capture your own genuine screenshot showing the Java source code in the editor and the actual execution output in the integrated terminal.

Note: Repeat these steps for Q1 to Q7, changing the question folder accordingly. Save each screenshot in its corresponding question's Screenshots folder. Do not use fabricated or edited execution screenshots.

## Task results

| Task | Result / verification |
|---|---|
| Q1 | Valid withdrawal and four failure paths tested. The `finally` block runs for all five test cases. |
| Q2 | Exam-closed, unregistered-student, oversized-file and storage-failure paths tested. Storage failure retains the original `IOException` as its cause. |
| Q3 | Valid reports match. Both resource-handling versions report zero open input files after the valid and corrupted-input tests. |
| Q4 | Runs ten trials for the `Thread` subclass and ten trials for `Runnable`, then tests priority and daemon behaviour. Output order can vary. |
| Q5 | Ten unsynchronized trials demonstrate duplicate booking; ten synchronized trials show zero duplicate bookings. |
| Q6 | Wallet transfers use consistent lock ordering. Producer-consumer total is 210; the bounded queue alternative is also demonstrated. |
| Q7 | Processes 10,000 generated script-file cases. The local run recorded 9,800 successes and 200 failures for each processing approach. |

## Q1 test inputs

| Test | Account number | Withdrawal | Card-reader file missing? |
|---|---:|---:|---|
| Valid withdrawal | `192411103` | `500` | `no` |
| Invalid numeric input | `abc` | Not requested | Not requested |
| Negative withdrawal | `192411103` | `-10` | `no` |
| Insufficient balance | `192411103` | `6000` | `no` |
| Missing card-reader file | `192411103` | `100` | `yes` |

## Screenshot note

The PNG files in the `Screenshots` folders are genuine output captures from local vs code executions of the included source code. They are **not screenshots from the AI**. Q1 includes five separate test-output captures.

## Submission checklist

- [ ] Add the GitHub repository URL to the cover page and submit it to faculty.
- [ ] Add your own Programiz screenshots if specifically required.
- [ ] Complete the report's Student A / Student B responsibilities with your pair partner.
- [ ] Include your pair-work contribution record.
- [ ] Commit each task progressively; this ZIP alone does not create GitHub commits.

**Important:** Timing values in `OUTPUT` are measurements from this local execution environment. They will vary with hardware, operating system and runtime load. Re-run the programs on your own system and use your own measured values in the final report.
