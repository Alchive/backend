package com.Alchive.backend.config;

import com.Alchive.backend.domain.algorithm.Algorithm;
import com.Alchive.backend.domain.board.Board;
import com.Alchive.backend.domain.board.BoardStatus;
import com.Alchive.backend.domain.problem.Problem;
import com.Alchive.backend.domain.problem.ProblemDifficulty;
import com.Alchive.backend.domain.problem.ProblemPlatform;
import com.Alchive.backend.domain.solution.Solution;
import com.Alchive.backend.domain.solution.SolutionLanguage;
import com.Alchive.backend.domain.solution.SolutionStatus;
import com.Alchive.backend.domain.user.User;
import com.Alchive.backend.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProblemRepository problemRepository;
    private final SolutionRepository solutionRepository;
    private final BoardRepository boardRepository;
    private final AlgorithmRepository algorithmRepository;
    private final AlgorithmProblemRepository algorithmProblemRepository;

    public DataInitializer(UserRepository userRepository,
                           ProblemRepository problemRepository,
                           SolutionRepository solutionRepository,
                           BoardRepository boardRepository,
                           AlgorithmRepository algorithmRepository,
                           AlgorithmProblemRepository algorithmProblemRepository) {
        this.userRepository = userRepository;
        this.problemRepository = problemRepository;
        this.solutionRepository = solutionRepository;
        this.boardRepository = boardRepository;
        this.algorithmRepository = algorithmRepository;
        this.algorithmProblemRepository = algorithmProblemRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // User 목업 데이터 생성
        User user1 = User.builder()
                .userEmail("chohana@alchive.com")
                .userNickName("조하나")
                .build();
        userRepository.save(user1);

        User user2 = User.builder()
                .userEmail("parknahyun@alchive.com")
                .userNickName("박나현")
                .build();
        userRepository.save(user2);

        User user3 = User.builder()
                .userEmail("songyurim@alchive.com")
                .userNickName("송유림")
                .build();
        userRepository.save(user3);

        User user4 = User.builder()
                .userEmail("kimmiyoung@alchive.com")
                .userNickName("김미영")
                .build();
        userRepository.save(user4);

        // Algorithm 목업 데이터 생성
        Algorithm algorithm1 = Algorithm.of("Binary Search");
        algorithmRepository.save(algorithm1);

        Algorithm algorithm2 = Algorithm.of("Dynamic Programming");
        algorithmRepository.save(algorithm2);

        Algorithm algorithm3 = Algorithm.of("DFS");
        algorithmRepository.save(algorithm3);

        Algorithm algorithm4 = Algorithm.of("BFS");
        algorithmRepository.save(algorithm4);

        // Problem1 목업 데이터 생성
        Problem problem1 = Problem.builder()
                .number(14670)
                .title("병약한 영정")
                .content("문제 설명<br>" +
                        "영정이는 병약하다. 본인도 병약한 것을 알고 있기 때문에 언제든 먹을 수 있는 약을 상비해서 들고 다닌다. 각 약들은 약의 이름과 필요한 증상이 적혀져 있으며 하나의 약은 하나의 증상만을 해결 할 수 있다. 각각의 약은 모두 다른 이름을 가지고 있으며 서로 다른 증상을 해결함이 보장된다. 영정이가 아픈 증상을 호소 할 때, 어떤 약들을 먹어야 하는지 출력 해 주자!<br><br>" +
                        "입력<br>" +
                        "프로그램의 입력은 표준 입력으로 받는다. 입력의 첫 줄에는 약의 종류의 개수 N이 입력된다. (1 ≤ N ≤ 100) 그 다음 N개의 줄에는 각각 약의 효능과 약의 이름이 숫자로 주어진다. (0 ≤ Me, Mn ≤ 100) 각 약의 이름과 효능은 다른 어떤 약의 이름, 효능과 중복되지 않음을 보장한다. 다음 줄에는 영정이가 겪는 증상의 개수 R이 입력된다. (1 ≤ R ≤ 100) 다음 R 줄에는 증상의 개수 Li 와 증상들 (S1, S2, … SL)이 주어진다 (1 ≤ Li ≤ N)<br><br>" +
                        "출력<br>" +
                        "프로그램의 출력은 표준 출력으로 한다. R 개의 증상에 따라 먹어야 하는 약을 순서대로 출력한다. 각각은 개행으로 구분한다. 증상을 하나 이상 해결할 수 없을 경우 ‘YOU DIED’ 를 출력한다. ")
                .url("https://www.acmicpc.net/problem/14670")
                .difficulty(ProblemDifficulty.LEVEL0)
                .platform(ProblemPlatform.BAEKJOON)
                .build();
        problemRepository.save(problem1);

        // Board1 목업 데이터 생성
        Board board1 = Board.builder()
                .problem(problem1)
                .user(user1)
                .memo("Hash Map을 사용하면 되지 않을까?")
                .status(BoardStatus.CORRECT)
                .description("사용 알고리즘: **Hash Map**")
                .build();
        boardRepository.save(board1);

        // Solution1 목업 데이터 생성
        Solution solution1 = Solution.builder()
                .content("#include &lt;iostream&gt;<br>" +
                        "#include &lt;algorithm&gt;<br>" +
                        "#include &lt;vector&gt;<br>" +
                        "#include &lt;string&gt;<br>" +
                        "#include &lt;map&gt;<br>" +
                        "#include &lt;queue&gt;<br>" +
                        "#include &lt;cmath&gt;<br>" +
                        "#define ll long long<br>" +
                        "using namespace std;<br><br>" +
                        "map&lt;int, int&gt; med;<br>" +
                        "int m[100];<br><br>" +
                        "int main() {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;ios_base::sync_with_stdio(0);<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;cin.tie(0); cout.tie(0);<br><br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;int n, me, mn, r, l, s;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;bool flag;<br><br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;cin &gt;&gt; n; // 약의 종류<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;for (int i = 0; i &lt; n; i++) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;cin &gt;&gt; me &gt;&gt; mn; // 약의 효능, 약의 이름<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;med[me] = mn; // hash map<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;}<br><br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;cin &gt;&gt; r; // 증상의 개수<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;for (int i = 0; i &lt; r; i++) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;cin &gt;&gt; l; // 증상의 개수<br><br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;flag = 1; // 초기화<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for (int k = 0; k &lt; l; k++) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;cin &gt;&gt; s;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;if (med.find(s) == med.end()) { flag = 0; } // 없음 died...<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;else { m[k] = med[s]; }<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;if (flag) { // 모두 치료 가능<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for (int z = 0; z &lt; l; z++) { cout &lt;&lt; m[z] &lt;&lt; \" \"; }<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;cout &lt;&lt; '\\n';<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;else { cout &lt;&lt; \"YOU DIED\\n\"; }<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;}<br><br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;return 0;<br>" +
                        "}<br>")
                .language(SolutionLanguage.CPP)
                .description("사용 알고리즘: Hash Map<br><br>" +
                        "반복문을 통해 약의 효능(key)과 이름(value)을 med(hash map)에 저장한다.<br>" +
                        "각 줄의 증상의 개수만큼 입력 받으면서 med에 등록된 효능이라면 배열에 약의 이름을 저장해준다.<br>" +
                        "저장되지 않은 효능이 나오면 flag에 false값을 저장한다.<br>" +
                        "flag가 참이면 배열에 저장된 약의 이름들을 형식에 맞게 출력한다.<br>" +
                        "flag가 거짓이면 YOU DIED를 출력한다.<br><br>" +
                        "3번 과정에서 저장되지 않은 효능이 나오면 flag를 수정하고 더이상 반복문을 돌리지 않는 것이 바람직하겠지만, 아래 코드에서 break만 추가하면 아직 입력되지 못한 값들이 다음으로 넘어가면서 프로그램이 꼬이게 된다.<br><br>" +
                        "그래서 지금 코드에서는 그냥 끝까지 돌려주는 로직으로 작성했지만 만약 범위가 더 커지면 시간 초과가 날 듯. 아무튼 비효율적임. 다음엔 이런 부분까지 생각하면서 코드를 짜보자!")
                .status(SolutionStatus.CORRECT)
                .memory(2024)
                .time(0)
                .submitAt(LocalDateTime.now())
                .board(board1) // 연결된 Board 예시 (먼저 Board 생성 필요)
                .build();
        solutionRepository.save(solution1);

        // Solution2 목업 데이터 생성
        Solution solution2 = Solution.builder()
                .content("#include &lt;iostream&gt;<br>" +
                        "#include &lt;algorithm&gt;<br>" +
                        "#include &lt;vector&gt;<br>" +
                        "#include &lt;string&gt;<br>" +
                        "#include &lt;map&gt;<br>" +
                        "#include &lt;queue&gt;<br>" +
                        "#include &lt;cmath&gt;<br>" +
                        "#define ll long long<br>" +
                        "using namespace std;<br><br>" +
                        "map&lt;int, int&gt; med;<br>" +
                        "int m[100];<br><br>" +
                        "int main() {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;ios_base::sync_with_stdio(0);<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;cin.tie(0); cout.tie(0);<br><br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;int n, me, mn, r, l, s, cnt;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;bool flag;<br><br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;cin &gt;&gt; n; // 약의 종류<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;for (int i = 0; i &lt; n; i++) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;cin &gt;&gt; me &gt;&gt; mn; // 약의 효능, 약의 이름<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;med[me] = mn; // hash map<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;}<br><br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;cin &gt;&gt; r; // 증상의 개수<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;for (int i = 0; i &lt; r; i++) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;cin &gt;&gt; l; // 증상의 개수<br><br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;flag = 1; // 초기화<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for (int k = 0; k &lt; l; k++) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;cin &gt;&gt; s;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;if (med[s] == NULL) { flag = 0; }<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;else { m[k] = med[s]; }<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;if (flag) { // 모두 치료 가능<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for (int z = 0; z &lt; l; z++) { cout &lt;&lt; m[z] &lt;&lt; \" \"; }<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;cout &lt;&lt; '\\n';<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;else { cout &lt;&lt; \"YOU DIED\\n\"; }<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;}<br><br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;return 0;<br>" +
                        "}<br>")
                .language(SolutionLanguage.CPP)
                .description("수많은 맞왜틀을 만들었던 코드.<br><br>" +
                        "if (med[s] == NULL)<br><br>" +
                        "약의 효능과 이름에는 0도 들어가는데 위의 조건문을 사용하면 0을 NULL 처리해버리고 있었음...<br>" +
                        "그래서 문제 풀이 1번의 코드처럼 med.find() == med.end()를 써서 처리했다.")
                .status(SolutionStatus.INCORRECT)
                .memory(2024)
                .time(0)
                .submitAt(LocalDateTime.now())
                .board(board1) // 연결된 Board 예시 (먼저 Board 생성 필요)
                .build();
        solutionRepository.save(solution2);

        // Problem2 목업 데이터 생성
        Problem problem2 = Problem.builder()
                .number(176963)
                .title("추억 점수")
                .content("문제 설명<br>" +
                        "사진들을 보며 추억에 젖어 있던 루는 사진별로 추억 점수를 매길려고 합니다. 사진 속에 나오는 인물의 그리움 점수를 모두 합산한 값이 해당 사진의 추억 점수가 됩니다. 예를 들어 사진 속 인물의 이름이 [\"may\", \"kein\", \"kain\"]이고 각 인물의 그리움 점수가 [5점, 10점, 1점]일 때 해당 사진의 추억 점수는 16(5 + 10 + 1)점이 됩니다. 다른 사진 속 인물의 이름이 [\"kali\", \"mari\", \"don\", \"tony\"]이고 [\"kali\", \"mari\", \"don\"]의 그리움 점수가 각각 [11점, 1점, 55점]]이고, \"tony\"는 그리움 점수가 없을 때, 이 사진의 추억 점수는 3명의 그리움 점수를 합한 67(11 + 1 + 55)점입니다.<br>" +
                        "<br>" +
                        "그리워하는 사람의 이름을 담은 문자열 배열 name, 각 사람별 그리움 점수를 담은 정수 배열 yearning, 각 사진에 찍힌 인물의 이름을 담은 이차원 문자열 배열 photo가 매개변수로 주어질 때, 사진들의 추억 점수를 photo에 주어진 순서대로 배열에 담아 return하는 solution 함수를 완성해주세요.<br>" +
                        "<br>" +
                        "제한사항<br>" +
                        "3 ≤ name의 길이 = yearning의 길이≤ 100<br>" +
                        "3 ≤ name의 원소의 길이 ≤ 7<br>" +
                        "name의 원소들은 알파벳 소문자로만 이루어져 있습니다.<br>" +
                        "name에는 중복된 값이 들어가지 않습니다.<br>" +
                        "1 ≤ yearning[i] ≤ 100<br>" +
                        "yearning[i]는 i번째 사람의 그리움 점수입니다.<br>" +
                        "3 ≤ photo의 길이 ≤ 100<br>" +
                        "1 ≤ photo[i]의 길이 ≤ 100<br>" +
                        "3 ≤ photo[i]의 원소(문자열)의 길이 ≤ 7<br>" +
                        "photo[i]의 원소들은 알파벳 소문자로만 이루어져 있습니다.<br>" +
                        "photo[i]의 원소들은 중복된 값이 들어가지 않습니다.<br>" +
                        "입출력 예<br>" +
                        "name&nbsp;&nbsp;&nbsp;&nbsp;yearning&nbsp;&nbsp;&nbsp;&nbsp;photo&nbsp;&nbsp;&nbsp;&nbsp;result<br>" +
                        "[\"may\", \"kein\", \"kain\", \"radi\"]&nbsp;&nbsp;&nbsp;&nbsp;[5, 10, 1, 3]&nbsp;&nbsp;&nbsp;&nbsp;[[\"may\", \"kein\", \"kain\", \"radi\"],[\"may\", \"kein\", \"brin\", \"deny\"], [\"kon\", \"kain\", \"may\", \"coni\"]]&nbsp;&nbsp;&nbsp;&nbsp;[19, 15, 6]<br>" +
                        "[\"kali\", \"mari\", \"don\"]&nbsp;&nbsp;&nbsp;&nbsp;[11, 1, 55]&nbsp;&nbsp;&nbsp;&nbsp;[[\"kali\", \"mari\", \"don\"], [\"pony\", \"tom\", \"teddy\"], [\"con\", \"mona\", \"don\"]]&nbsp;&nbsp;&nbsp;&nbsp;[67, 0, 55]<br>" +
                        "[\"may\", \"kein\", \"kain\", \"radi\"]&nbsp;&nbsp;&nbsp;&nbsp;[5, 10, 1, 3]&nbsp;&nbsp;&nbsp;&nbsp;[[\"may\"],[\"kein\", \"deny\", \"may\"], [\"kon\", \"coni\"]]&nbsp;&nbsp;&nbsp;&nbsp;[5, 15, 0]<br>" +
                        "입출력 예 설명<br>" +
                        "입출력 예 #1<br>" +
                        "첫 번째 사진 속 \"may\", \"kein\", \"kain\", \"radi\"의 그리움 점수를 합치면 19(5 + 10 + 1 + 3)점 입니다. 두 번째 사진 속 그리워하는 사람들인 \"may\"와 \"kein\"의 그리움 점수를 합치면 15(5 + 10)점입니다. 세 번째 사진의 경우 \"kain\"과 \"may\"만 그리워하므로 둘의 그리움 점수를 합한 6(1 + 5)점이 사진의 추억 점수입니다. 따라서 [19, 15, 6]을 반환합니다.<br>" +
                        "<br>" +
                        "입출력 예 #2<br>" +
                        "첫 번째 사진 속 그리워하는 사람들인 \"kali\", \"mari\", \"don\"의 그리움 점수를 합치면 67(11 + 1 + 55)점입니다. 두 번째 사진 속엔 그리워하는 인물이 없으므로 0점입니다. 세 번째 사진 속 그리워하는 사람은 \"don\"만 있으므로 55점입니다. 따라서 [67, 0, 55]를 반환합니다.<br>" +
                        "<br>" +
                        "입출력 예 #3<br>" +
                        "설명 생략")
                .url("https://school.programmers.co.kr/learn/courses/30/lessons/176963")
                .difficulty(ProblemDifficulty.LEVEL1)
                .platform(ProblemPlatform.PROGRAMMERS)
                .build();
        problemRepository.save(problem2);

        // Board2 목업 데이터 생성
        Board board2 = Board.builder()
                .problem(problem2)
                .user(user1)
                .memo("자스로도 다시 풀어봐야겠다")
                .status(BoardStatus.CORRECT)
                .description("사진 속 인물은 여러 명이지만 모두를 그리워하진 않는다.<br>" +
                        "그리워하는 인물들은 따로 정해져있는데 이 인물들마다 그리움 점수가 있다.<br>" +
                        "<br>" +
                        "이 그리워하는 인물들은 2차원 배열 안에 그룹별로 흩어져있다.<br>" +
                        "그룹별로 그리움 점수를 합산<br>" +
                        "<br>" +
                        "<br>" +
                        "그리워하는 이름들이랑 2차원 배열들이랑 일일이 비교하면 되지 않을까? 라는 생각이 들었는데 그렇게 단순한 문제면 일일이 비교했을 때 시간제한이 걸리게 만들어 놓지 않았을까? → 그런 거 없이 삼중 for문으로도 그냥 풀림, 예전에 정답률 50% 문제에도 끙끙 앓았던 적이 있어서 이 문제도 과대평가한 듯<br>" +
                        "<br>" +
                        "<br>" +
                        "name과 yearning 매칭은 map 사용하거나 for문은 같은 인덱스 별로 묶어 계산<br>" +
                        "배열은 0부터 N까지 돌아가면서 탐색<br>" +
                        "map은 해당 key에 있는 value를 바로 가져온다 → 속도 빠름")
                .build();
        boardRepository.save(board2);

        // Solution21 목업 데이터 생성
        Solution solution21 = Solution.builder()
                .content("class Solution {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;public int[] solution(String[] name, int[] yearning, String[][] photo) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;int[] answer = new int[photo.length];<br>" +
                        "<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for(int i=0; i&lt;photo.length; i++) { // photo 요소<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for(int j=0; j&lt;photo[i].length; j++) { // photo 요소의 요소? 뭐라 부르지;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for(int k=0; k&lt;name.length; k++) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;if(photo[i][j].equals(name[k])) answer[i] += yearning[k];<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;return answer;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "}")
                .language(SolutionLanguage.JAVA)
                .description("for문 사용한 풀이")
                .status(SolutionStatus.CORRECT)
                .memory(256)
                .time(100)
                .submitAt(LocalDateTime.now())
                .board(board2) // 연결된 Board 예시 (먼저 Board 생성 필요)
                .build();
        solutionRepository.save(solution21);

        // Solution22 목업 데이터 생성
        Solution solution22 = Solution.builder()
                .content("class Solution{<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;public int[] solution(String[] name, int[] yearning, String[][] photo) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;int[] answer = new int[photo.length];<br>" +
                        "<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;HashMap&lt;String,Integer&gt; map = new LinkedHashMap&lt;&gt;();<br>" +
                        "<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for(int i=0; i&lt; name.length; i++){<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;map.put(name[i], yearning[i]); // 추억하는 사람 이름에 점수 저장<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for(int i=0; i&lt; photo.length; i++){<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;String[] persons = photo[i]; // 그룹별 배열 생성<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;int score = 0;<br>" +
                        "<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for(int j=0; j&lt;persons.length; j++){<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;String person = persons[j];<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;if(map.containsKey(person)){ // map의 key에 photo 속 사람이 있다면<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;score+=map.get(person); // 점수 추가<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;answer[i]=score;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;return answer;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "}")
                .language(SolutionLanguage.JAVA)
                .description("다른 사람 풀이<br>" +
                        "단순 배열보단 map을 활용한 풀이가 빨라서 그런지 다른 사람들은 map을 많이 활용하였더라")
                .status(SolutionStatus.CORRECT)
                .memory(256)
                .time(100)
                .submitAt(LocalDateTime.now())
                .board(board2) // 연결된 Board 예시 (먼저 Board 생성 필요)
                .build();
        solutionRepository.save(solution22);

        // Problem3 목업 데이터 생성
        Problem problem3 = Problem.builder()
                .number(2003)
                .title("수들의 합")
                .content("N개의 수로 된 수열 A[1], A[2], …, A[N]이 있다. 이 수열의 i번째 수부터 j번째 수까지의 합이 M이 되는 경우의 수를 구해보자.")
                .url("https://www.acmicpc.net/problem/2003")
                .difficulty(ProblemDifficulty.LEVEL1)
                .platform(ProblemPlatform.BAEKJOON)
                .build();
        problemRepository.save(problem3);

        // Board3 목업 데이터 생성
        Board board3 = Board.builder()
                .problem(problem3)
                .user(user1)
                .memo("Two Pointer를 사용해보자")
                .status(BoardStatus.CORRECT)
                .description("사용 알고리즘: Two Pointer")
                .build();
        boardRepository.save(board3);

        // Solution3 목업 데이터 생성
        Solution solution3 = Solution.builder()
                .content("import sys<br>" +
                        "input = sys.stdin.readline<br>" +
                        "<br>" +
                        "n, m = map(int, input().split())<br>" +
                        "data = list(map(int, input().split()))<br>" +
                        "<br>" +
                        "count = 0<br>" +
                        "interval_sum = 0<br>" +
                        "end = 0<br>" +
                        "<br>" +
                        "for start in range(n):<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;while interval_sum &lt; m and end &lt; n:<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;interval_sum += data[end]<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;end += 1<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;if interval_sum == m:<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;count += 1<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;interval_sum -= data[start]<br>" +
                        "<br>" +
                        "print(count)")
                .language(SolutionLanguage.PYTHON)
                .description("Two Pointer를 이용한 부분합 문제 풀이")
                .status(SolutionStatus.CORRECT)
                .memory(128)
                .time(0)
                .submitAt(LocalDateTime.now())
                .board(board3)
                .build();
        solutionRepository.save(solution3);

        // Problem4 목업 데이터 생성
        Problem problem4 = Problem.builder()
                .number(42587)
                .title("프린터")
                .content("일렬로 인쇄할 문서들이 있으며, 각 문서는 중요도라는 값을 가집니다. 특정 문서를 인쇄하기 위해 대기열을 순서대로 처리하며, 대기열에서 우선순위가 가장 높은 문서가 인쇄됩니다. 이를 통해 요청한 특정 문서가 몇 번째로 인쇄되는지 구하는 문제입니다.")
                .url("https://programmers.co.kr/learn/courses/30/lessons/42587")
                .difficulty(ProblemDifficulty.LEVEL2)
                .platform(ProblemPlatform.PROGRAMMERS)
                .build();
        problemRepository.save(problem4);

        // Board4 목업 데이터 생성
        Board board4 = Board.builder()
                .problem(problem4)
                .user(user1)
                .memo("큐와 우선순위 큐 개념 복습 필요")
                .status(BoardStatus.CORRECT)
                .description("사용 알고리즘: Queue, Priority Queue")
                .build();
        boardRepository.save(board4);

        // Solution4 목업 데이터 생성
        Solution solution4 = Solution.builder()
                .content("import java.util.*;<br>" +
                        "class Solution {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;public int solution(int[] priorities, int location) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Queue&lt;int[]> queue = new LinkedList&lt;&gt;();<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for (int i = 0; i < priorities.length; i++) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;queue.add(new int[] { priorities[i], i });<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;int answer = 0;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;while (!queue.isEmpty()) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;int[] current = queue.poll();<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;boolean hasHigherPriority = false;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for (int[] q : queue) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;if (q[0] > current[0]) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;hasHigherPriority = true;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;break;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;if (hasHigherPriority) queue.add(current);<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;else {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;answer++;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;if (current[1] == location) return answer;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;return answer;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "}")
                .language(SolutionLanguage.JAVA)
                .description("큐를 사용해 각 문서의 중요도와 위치를 큐에 저장한 후, 큐를 돌며 우선순위가 가장 높은 문서가 나올 때까지 대기시킴. 우선순위가 높은 문서가 있으면 해당 문서를 뒤로 보냄. 지정된 문서가 출력될 때까지 반복.")
                .status(SolutionStatus.CORRECT)
                .memory(256)
                .time(100)
                .submitAt(LocalDateTime.now())
                .board(board4)
                .build();
        solutionRepository.save(solution4);

// Problem5 목업 데이터 생성
        Problem problem5 = Problem.builder()
                .number(42586)
                .title("기능개발")
                .content("각 기능은 작업의 진도와 작업 속도를 갖고 있으며, 작업이 100% 완료되면 배포됩니다. 먼저 완료된 작업부터 순서대로 배포하며, 같은 날 배포할 수 있는 작업은 묶어서 배포합니다. 기능별 작업 완료일을 예측해 며칠씩 배포될지 계산하는 문제입니다.")
                .url("https://programmers.co.kr/learn/courses/30/lessons/42586")
                .difficulty(ProblemDifficulty.LEVEL2)
                .platform(ProblemPlatform.PROGRAMMERS)
                .build();
        problemRepository.save(problem5);

        // Board5 목업 데이터 생성
        Board board5 = Board.builder()
                .problem(problem5)
                .user(user1)
                .memo("작업 속도와 진도 계산 방법 이해 필요")
                .status(BoardStatus.CORRECT)
                .description("사용 알고리즘: Queue")
                .build();
        boardRepository.save(board5);

        // Solution5 목업 데이터 생성
        Solution solution5 = Solution.builder()
                .content("import java.util.*;<br>" +
                        "class Solution {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;public int[] solution(int[] progresses, int[] speeds) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Queue&lt;Integer&gt; days = new LinkedList&lt;&gt;();<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for (int i = 0; i < progresses.length; i++) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;int day = (100 - progresses[i]) / speeds[i];<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;if ((100 - progresses[i]) % speeds[i] != 0) day++;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;days.add(day);<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;List&lt;Integer&gt; result = new ArrayList&lt;&gt;();<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;int current = days.poll();<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;int count = 1;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;while (!days.isEmpty()) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;int next = days.poll();<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;if (next <= current) count++;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;else {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;result.add(count);<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;count = 1;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;current = next;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;result.add(count);<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;return result.stream().mapToInt(i -> i).toArray();<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "}")
                .language(SolutionLanguage.JAVA)
                .description("각 작업의 남은 진도를 작업 속도로 나누어 완료까지 필요한 일수를 계산한 후, 다음 작업을 현재 작업과 비교하여 동시에 배포 가능한 경우를 체크하며 카운트를 증가시킴. 새로운 배포가 필요할 때마다 결과 리스트에 추가.")
                .status(SolutionStatus.CORRECT)
                .memory(256)
                .time(100)
                .submitAt(LocalDateTime.now())
                .board(board5)
                .build();
        solutionRepository.save(solution5);

        // Problem6 목업 데이터 생성
        Problem problem6 = Problem.builder()
                .number(12909)
                .title("올바른 괄호")
                .content("주어진 문자열이 올바른 괄호 문자열인지 확인하는 문제입니다. 괄호가 열리면 닫혀야 하고, 닫히기 전에는 다시 열리지 않아야 합니다.")
                .url("https://programmers.co.kr/learn/courses/30/lessons/12909")
                .difficulty(ProblemDifficulty.LEVEL2)
                .platform(ProblemPlatform.PROGRAMMERS)
                .build();
        problemRepository.save(problem6);

        // Board6 목업 데이터 생성
        Board board6 = Board.builder()
                .problem(problem6)
                .user(user1)
                .memo("Stack 자료구조로 해결해보자")
                .status(BoardStatus.CORRECT)
                .description("사용 알고리즘: Stack")
                .build();
        boardRepository.save(board6);

        // Solution6 목업 데이터 생성
        Solution solution6 = Solution.builder()
                .content("import java.util.*;<br>" +
                        "class Solution {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;boolean solution(String s) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Stack&lt;Character&gt; stack = new Stack&lt;&gt;();<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for (char c : s.toCharArray()) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;if (c == '(') stack.push(c);<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;else {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;if (stack.isEmpty()) return false;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;stack.pop();<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;return stack.isEmpty();<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "}")
                .language(SolutionLanguage.JAVA)
                .description("열린 괄호는 스택에 추가하고, 닫힌 괄호는 스택에서 제거하며, 스택이 비어 있는 상태에서 닫힌 괄호가 등장하면 false 반환. 모든 문자가 처리된 후 스택이 비어 있다면 올바른 괄호 문자열.")
                .status(SolutionStatus.CORRECT)
                .memory(128)
                .time(70)
                .submitAt(LocalDateTime.now())
                .board(board6)
                .build();
        solutionRepository.save(solution6);

// Problem7 목업 데이터 생성
        Problem problem7 = Problem.builder()
                .number(12945)
                .title("피보나치 수")
                .content("주어진 수 n에 대해 n번째 피보나치 수를 구하는 문제입니다. n번째 피보나치 수를 1234567로 나눈 나머지를 반환합니다.")
                .url("https://programmers.co.kr/learn/courses/30/lessons/12945")
                .difficulty(ProblemDifficulty.LEVEL2)
                .platform(ProblemPlatform.PROGRAMMERS)
                .build();
        problemRepository.save(problem7);

        // Board7 목업 데이터 생성
        Board board7 = Board.builder()
                .problem(problem7)
                .user(user1)
                .memo("Dynamic Programming으로 해결 가능")
                .status(BoardStatus.CORRECT)
                .description("사용 알고리즘: Dynamic Programming")
                .build();
        boardRepository.save(board7);

        // Solution7 목업 데이터 생성
        Solution solution7 = Solution.builder()
                .content("class Solution {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;public int solution(int n) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;if (n == 0) return 0;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;if (n == 1) return 1;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;int[] dp = new int[n + 1];<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;dp[0] = 0;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;dp[1] = 1;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for (int i = 2; i <= n; i++) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;dp[i] = (dp[i - 1] + dp[i - 2]) % 1234567;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;return dp[n];<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "}")
                .language(SolutionLanguage.JAVA)
                .description("Dynamic Programming을 사용하여 피보나치 수를 구하며, 큰 수로 인해 값이 커질 때마다 1234567로 나눈 나머지를 저장함.")
                .status(SolutionStatus.CORRECT)
                .memory(256)
                .time(90)
                .submitAt(LocalDateTime.now())
                .board(board7)
                .build();
        solutionRepository.save(solution7);

// Problem8 목업 데이터 생성
        Problem problem8 = Problem.builder()
                .number(12950)
                .title("행렬의 덧셈")
                .content("두 개의 행렬이 주어졌을 때, 행렬을 더하는 문제입니다. 주어진 행렬 A와 B를 더하여 새로운 행렬을 반환합니다.")
                .url("https://programmers.co.kr/learn/courses/30/lessons/12950")
                .difficulty(ProblemDifficulty.LEVEL1)
                .platform(ProblemPlatform.PROGRAMMERS)
                .build();
        problemRepository.save(problem8);

        // Board8 목업 데이터 생성
        Board board8 = Board.builder()
                .problem(problem8)
                .user(user1)
                .memo("2차원 배열에 대한 이해 필요")
                .status(BoardStatus.CORRECT)
                .description("사용 알고리즘: Array")
                .build();
        boardRepository.save(board8);

        // Solution8 목업 데이터 생성
        Solution solution8 = Solution.builder()
                .content("class Solution {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;public int[][] solution(int[][] arr1, int[][] arr2) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;int[][] answer = new int[arr1.length][arr1[0].length];<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for (int i = 0; i < arr1.length; i++) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for (int j = 0; j < arr1[0].length; j++) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;answer[i][j] = arr1[i][j] + arr2[i][j];<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;return answer;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "}")
                .language(SolutionLanguage.JAVA)
                .description("2차원 배열의 각 요소를 순차적으로 더하여 새로운 2차원 배열에 저장하고 반환하는 방식으로 풀이.")
                .status(SolutionStatus.CORRECT)
                .memory(128)
                .time(60)
                .submitAt(LocalDateTime.now())
                .board(board8)
                .build();
        solutionRepository.save(solution8);

// Problem9 목업 데이터 생성
        Problem problem9 = Problem.builder()
                .number(43165)
                .title("타겟 넘버")
                .content("n개의 숫자로 이루어진 배열에서 '+'와 '-' 연산을 조합하여 타겟 넘버를 만드는 방법의 수를 찾는 문제입니다.")
                .url("https://programmers.co.kr/learn/courses/30/lessons/43165")
                .difficulty(ProblemDifficulty.LEVEL2)
                .platform(ProblemPlatform.PROGRAMMERS)
                .build();
        problemRepository.save(problem9);

        // Board9 목업 데이터 생성
        Board board9 = Board.builder()
                .problem(problem9)
                .user(user1)
                .memo("DFS/BFS 재귀적으로 구현해보기")
                .status(BoardStatus.CORRECT)
                .description("사용 알고리즘: DFS, BFS")
                .build();
        boardRepository.save(board9);

        // Solution9 목업 데이터 생성
        Solution solution9 = Solution.builder()
                .content("class Solution {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;int answer = 0;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;public int solution(int[] numbers, int target) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;dfs(numbers, target, 0, 0);<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;return answer;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;public void dfs(int[] numbers, int target, int index, int sum) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;if (index == numbers.length) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;if (sum == target) answer++;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;return;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;dfs(numbers, target, index + 1, sum + numbers[index]);<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;dfs(numbers, target, index + 1, sum - numbers[index]);<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "}")
                .language(SolutionLanguage.JAVA)
                .description("DFS를 이용해 각 숫자에 대해 더하거나 빼는 모든 경우의 수를 탐색하여 타겟 넘버를 만들 수 있는 경우를 카운트.")
                .status(SolutionStatus.CORRECT)
                .memory(256)
                .time(120)
                .submitAt(LocalDateTime.now())
                .board(board9)
                .build();
        solutionRepository.save(solution9);

// Problem10 목업 데이터 생성
        Problem problem10 = Problem.builder()
                .number(42842)
                .title("카펫")
                .content("갈색과 노란색 격자가 주어질 때, 주어진 격자를 만족하는 카펫의 가로와 세로 크기를 구하는 문제입니다.")
                .url("https://programmers.co.kr/learn/courses/30/lessons/42842")
                .difficulty(ProblemDifficulty.LEVEL2)
                .platform(ProblemPlatform.PROGRAMMERS)
                .build();
        problemRepository.save(problem10);

        // Board10 목업 데이터 생성
        Board board10 = Board.builder()
                .problem(problem10)
                .user(user1)
                .memo("완전탐색으로 해결 가능")
                .status(BoardStatus.CORRECT)
                .description("사용 알고리즘: Brute Force")
                .build();
        boardRepository.save(board10);

        // Solution10 목업 데이터 생성
        Solution solution10 = Solution.builder()
                .content("class Solution {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;public int[] solution(int brown, int yellow) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for (int height = 3; height <= (brown + yellow) / height; height++) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;if ((brown + yellow) % height == 0) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;int width = (brown + yellow) / height;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;if (width >= height && (width - 2) * (height - 2) == yellow) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;return new int[] {width, height};<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;return new int[0];<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "}")
                .language(SolutionLanguage.JAVA)
                .description("완전탐색으로 카펫의 높이와 너비를 조합하여 조건에 맞는 경우를 찾고, 정답으로 반환하는 방식으로 풀이.")
                .status(SolutionStatus.CORRECT)
                .memory(128)
                .time(110)
                .submitAt(LocalDateTime.now())
                .board(board10)
                .build();
        solutionRepository.save(solution10);

        // Problem11 목업 데이터 생성
        Problem problem11 = Problem.builder()
                .number(43238)
                .title("입국심사")
                .content("각 입국 심사관이 심사를 마치는 데 걸리는 시간이 다를 때, N명의 사람들이 주어진 심사관으로 모두 심사를 완료하는 데 필요한 최소 시간을 구하는 문제입니다. 예를 들어, 심사관이 각각 7분과 10분을 걸린다면, 6명이 심사를 마치는 최소 시간은 28분이 됩니다. 심사관이 더 많은 사람을 처리할 수 있도록 배치하여 최적의 시간을 계산해야 합니다.")
                .url("https://programmers.co.kr/learn/courses/30/lessons/43238")
                .difficulty(ProblemDifficulty.LEVEL3)
                .platform(ProblemPlatform.PROGRAMMERS)
                .build();
        problemRepository.save(problem11);

        // Board11 목업 데이터 생성
        Board board11 = Board.builder()
                .problem(problem11)
                .user(user1)
                .memo("이진 탐색을 통해 효율적으로 풀이 가능")
                .status(BoardStatus.CORRECT)
                .description("사용 알고리즘: Binary Search")
                .build();
        boardRepository.save(board11);

        // Solution11 목업 데이터 생성
        Solution solution11 = Solution.builder()
                .content("import java.util.*;<br>" +
                        "class Solution {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;public long solution(int n, int[] times) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Arrays.sort(times);<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;long left = 1;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;long right = (long) times[times.length - 1] * n;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;while (left < right) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;long mid = (left + right) / 2;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;long sum = 0;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for (int time : times) sum += mid / time;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;if (sum >= n) right = mid;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;else left = mid + 1;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;return left;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "}")
                .language(SolutionLanguage.JAVA)
                .description("이진 탐색을 사용하여 심사에 필요한 최소 시간을 구함. 각 중간 시간을 기준으로 모든 심사관이 몇 명의 사람을 처리할 수 있는지 계산하여 목표 인원에 도달할 때까지 반복.")
                .status(SolutionStatus.CORRECT)
                .memory(512)
                .time(130)
                .submitAt(LocalDateTime.now())
                .board(board11)
                .build();
        solutionRepository.save(solution11);

// Problem12 목업 데이터 생성
        Problem problem12 = Problem.builder()
                .number(60057)
                .title("문자열 압축")
                .content("주어진 문자열을 연속된 문자열을 압축하여 가장 짧은 길이로 만드는 문제입니다. 예를 들어, 문자열이 \"aabbaccc\"일 경우 \"2a2ba3c\"와 같이 압축할 수 있으며, 다양한 단위로 압축하여 최적의 길이를 찾는 과정을 요구합니다.")
                .url("https://programmers.co.kr/learn/courses/30/lessons/60057")
                .difficulty(ProblemDifficulty.LEVEL2)
                .platform(ProblemPlatform.PROGRAMMERS)
                .build();
        problemRepository.save(problem12);

        // Board12 목업 데이터 생성
        Board board12 = Board.builder()
                .problem(problem12)
                .user(user1)
                .memo("압축 단위별로 반복적으로 확인 필요")
                .status(BoardStatus.CORRECT)
                .description("사용 알고리즘: String Manipulation, Brute Force")
                .build();
        boardRepository.save(board12);

        // Solution12 목업 데이터 생성
        Solution solution12 = Solution.builder()
                .content("class Solution {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;public int solution(String s) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;int minLength = s.length();<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for (int step = 1; step <= s.length() / 2; step++) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;StringBuilder compressed = new StringBuilder();<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;String prev = s.substring(0, step);<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;int count = 1;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for (int j = step; j < s.length(); j += step) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;String next = s.substring(j, Math.min(j + step, s.length()));<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;if (prev.equals(next)) count++;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;else {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;compressed.append(count > 1 ? count : \"\").append(prev);<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;prev = next;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;count = 1;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;compressed.append(count > 1 ? count : \"\").append(prev);<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;minLength = Math.min(minLength, compressed.length());<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;return minLength;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "}")
                .language(SolutionLanguage.JAVA)
                .description("문자열을 1부터 n/2까지의 단위로 압축하여 최적의 길이를 찾음. 반복되는 패턴을 찾아 압축한 후, 가장 짧은 문자열 길이를 반환.")
                .status(SolutionStatus.CORRECT)
                .memory(512)
                .time(150)
                .submitAt(LocalDateTime.now())
                .board(board12)
                .build();
        solutionRepository.save(solution12);

// Problem13 목업 데이터 생성
        Problem problem13 = Problem.builder()
                .number(64065)
                .title("튜플")
                .content("집합을 이용하여 주어진 튜플을 순서대로 배열로 반환하는 문제입니다. 튜플의 요소들이 중복되지 않고 주어진 입력을 분석하여 순서를 찾는 것이 핵심입니다.")
                .url("https://programmers.co.kr/learn/courses/30/lessons/64065")
                .difficulty(ProblemDifficulty.LEVEL2)
                .platform(ProblemPlatform.PROGRAMMERS)
                .build();
        problemRepository.save(problem13);

        // Board13 목업 데이터 생성
        Board board13 = Board.builder()
                .problem(problem13)
                .user(user1)
                .memo("주어진 문자열 파싱과 순서 찾기 필요")
                .status(BoardStatus.CORRECT)
                .description("사용 알고리즘: String Parsing, Set")
                .build();
        boardRepository.save(board13);

        // Solution13 목업 데이터 생성
        Solution solution13 = Solution.builder()
                .content("import java.util.*;<br>" +
                        "class Solution {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;public int[] solution(String s) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;s = s.replace(\"{{\", \"\").replace(\"}}\", \"\");<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;String[] sets = s.split(\"},\");<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Arrays.sort(sets, Comparator.comparingInt(String::length));<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;List&lt;Integer&gt; result = new ArrayList&lt;&gt;();<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Set&lt;Integer&gt; set = new HashSet&lt;&gt;();<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for (String str : sets) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for (String num : str.split(\",\")) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;int n = Integer.parseInt(num.trim());<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;if (set.add(n)) result.add(n);<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;return result.stream().mapToInt(i -> i).toArray();<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "}")
                .language(SolutionLanguage.JAVA)
                .description("문자열을 파싱하여 중복을 제거하고 순서대로 배열을 구성. 각 튜플을 집합으로 만들어 길이순으로 정렬한 후, 순서대로 값을 배열에 추가하여 반환.")
                .status(SolutionStatus.CORRECT)
                .memory(512)
                .time(140)
                .submitAt(LocalDateTime.now())
                .board(board13)
                .build();
        solutionRepository.save(solution13);

// Problem14 목업 데이터 생성
        Problem problem14 = Problem.builder()
                .number(42839)
                .title("소수 찾기")
                .content("주어진 숫자 조합으로 만들 수 있는 소수의 개수를 찾는 문제입니다. 주어진 숫자들로 서로 다른 숫자 조합을 생성하고, 해당 조합이 소수인지 판단하여 소수인 경우를 카운트합니다.")
                .url("https://programmers.co.kr/learn/courses/30/lessons/42839")
                .difficulty(ProblemDifficulty.LEVEL2)
                .platform(ProblemPlatform.PROGRAMMERS)
                .build();
        problemRepository.save(problem14);

        // Board14 목업 데이터 생성
        Board board14 = Board.builder()
                .problem(problem14)
                .user(user1)
                .memo("백트래킹을 이용해 조합 생성 필요")
                .status(BoardStatus.CORRECT)
                .description("사용 알고리즘: Backtracking, Set")
                .build();
        boardRepository.save(board14);

        // Solution14 목업 데이터 생성
        Solution solution14 = Solution.builder()
                .content("import java.util.*;<br>" +
                        "class Solution {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;Set&lt;Integer&gt; primes = new HashSet&lt;&gt;();<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;public int solution(String numbers) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;backtrack(numbers.toCharArray(), new boolean[numbers.length()], \"\");<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;return primes.size();<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;void backtrack(char[] chars, boolean[] used, String current) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;if (!current.isEmpty()) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;int num = Integer.parseInt(current);<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;if (isPrime(num)) primes.add(num);<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for (int i = 0; i < chars.length; i++) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;if (!used[i]) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;used[i] = true;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;backtrack(chars, used, current + chars[i]);<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;used[i] = false;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;boolean isPrime(int n) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;if (n < 2) return false;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for (int i = 2; i <= Math.sqrt(n); i++)<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;if (n % i == 0) return false;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;return true;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "}")
                .language(SolutionLanguage.JAVA)
                .description("백트래킹을 사용해 모든 숫자 조합을 생성하고, 각 조합이 소수인지 판단하여 소수인 경우만 Set에 추가. 마지막에 Set 크기를 반환.")
                .status(SolutionStatus.CORRECT)
                .memory(512)
                .time(160)
                .submitAt(LocalDateTime.now())
                .board(board14)
                .build();
        solutionRepository.save(solution14);

        // Problem15 목업 데이터 생성
        Problem problem15 = Problem.builder()
                .number(49994)
                .title("방문 길이")
                .content("캐릭터가 주어진 명령어에 따라 움직일 때, 새로운 길을 방문한 횟수를 계산하는 문제입니다. 명령어 U, D, L, R을 통해 캐릭터는 이동하며, 격자 밖으로는 이동할 수 없고, 중복된 길 방문은 횟수에 포함되지 않습니다.")
                .url("https://programmers.co.kr/learn/courses/30/lessons/49994")
                .difficulty(ProblemDifficulty.LEVEL2)
                .platform(ProblemPlatform.PROGRAMMERS)
                .build();
        problemRepository.save(problem15);

        // Board15 목업 데이터 생성
        Board board15 = Board.builder()
                .problem(problem15)
                .user(user1)
                .memo("좌표와 Set을 이용해 방문 체크 필요")
                .status(BoardStatus.CORRECT)
                .description("사용 알고리즘: Set, 2D Coordinates")
                .build();
        boardRepository.save(board15);

        // Solution15 목업 데이터 생성
        Solution solution15 = Solution.builder()
                .content("import java.util.*;<br>" +
                        "class Solution {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;public int solution(String dirs) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Set&lt;String&gt; visited = new HashSet&lt;&gt;();<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;int x = 0, y = 0;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for (char dir : dirs.toCharArray()) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;int nx = x, ny = y;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;switch (dir) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;case 'U': ny++; break;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;case 'D': ny--; break;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;case 'L': nx--; break;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;case 'R': nx++; break;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;if (nx &lt; -5 || nx &gt; 5 || ny &lt; -5 || ny &gt; 5) continue;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;String path = x + \"\" + y + \"\" + nx + \"\" + ny;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;String reversePath = nx + \"\" + ny + \"\" + x + \"\" + y;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;visited.add(path);<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;visited.add(reversePath);<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;x = nx; y = ny;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;return visited.size() / 2;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "}")
                .language(SolutionLanguage.JAVA)
                .description("방문한 길을 좌표로 저장하여 중복 방문을 피함. 각 이동 명령어에 따라 새 좌표와 길을 확인하여 새로운 길만 카운트.")
                .status(SolutionStatus.CORRECT)
                .memory(512)
                .time(150)
                .submitAt(LocalDateTime.now())
                .board(board15)
                .build();
        solutionRepository.save(solution15);

        // Problem16 목업 데이터 생성
        Problem problem16 = Problem.builder()
                .number(42890)
                .title("후보키")
                .content("주어진 테이블에서 속성들의 조합으로 후보 키를 찾는 문제입니다. 주어진 데이터베이스에서 유일성을 만족하면서도 최소성을 만족하는 속성 조합을 후보 키로 간주합니다. 예를 들어 학생 데이터베이스에서 학번과 이름 조합이 유일성을 만족한다면, 이를 후보 키로 볼 수 있습니다.")
                .url("https://programmers.co.kr/learn/courses/30/lessons/42890")
                .difficulty(ProblemDifficulty.LEVEL3)
                .platform(ProblemPlatform.PROGRAMMERS)
                .build();
        problemRepository.save(problem16);

        // Board16 목업 데이터 생성
        Board board16 = Board.builder()
                .problem(problem16)
                .user(user1)
                .memo("유일성과 최소성을 모두 만족하는 조합 찾기 필요")
                .status(BoardStatus.CORRECT)
                .description("사용 알고리즘: Bit Masking, Set, Combination")
                .build();
        boardRepository.save(board16);

        // Solution16 목업 데이터 생성
        Solution solution16 = Solution.builder()
                .content("import java.util.*;<br>" +
                        "class Solution {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;public int solution(String[][] relation) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;int rowLen = relation.length;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;int colLen = relation[0].length;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;List&lt;Integer&gt; candidateKeys = new ArrayList&lt;&gt;();<br>" +
                        "<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for (int i = 1; i < (1 << colLen); i++) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Set&lt;String&gt; set = new HashSet&lt;&gt;();<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for (int j = 0; j < rowLen; j++) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;StringBuilder sb = new StringBuilder();<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for (int k = 0; k < colLen; k++)<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;if ((i & (1 << k)) != 0) sb.append(relation[j][k]).append(\",\");<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;set.add(sb.toString());<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;if (set.size() == rowLen && isMinimal(i, candidateKeys))<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;candidateKeys.add(i);<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;return candidateKeys.size();<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;private boolean isMinimal(int subset, List&lt;Integer&gt; keys) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for (int key : keys)<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;if ((key & subset) == key) return false;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;return true;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "}")
                .language(SolutionLanguage.JAVA)
                .description("비트 마스킹을 사용해 각 속성 조합을 표현하고, 유일성과 최소성을 만족하는 후보 키 조합을 찾음. 모든 조합을 검사하여 유일성을 확인한 후, 최소성 검사를 통해 후보 키를 최종 선정함.")
                .status(SolutionStatus.CORRECT)
                .memory(1024)
                .time(200)
                .submitAt(LocalDateTime.now())
                .board(board16)
                .build();
        solutionRepository.save(solution16);

// Problem17 목업 데이터 생성
        Problem problem17 = Problem.builder()
                .number(64064)
                .title("불량 사용자")
                .content("아이디 목록에서 특정 조건에 맞는 사용자 조합을 찾는 문제입니다. 주어진 제약 조건에 맞게 선택할 수 있는 사용자 조합의 수를 반환하며, 제약 조건으로는 와일드카드 문자를 포함할 수 있습니다. 각 와일드카드 문자에 맞는 조합을 통해 불량 사용자 수를 계산합니다.")
                .url("https://programmers.co.kr/learn/courses/30/lessons/64064")
                .difficulty(ProblemDifficulty.LEVEL3)
                .platform(ProblemPlatform.PROGRAMMERS)
                .build();
        problemRepository.save(problem17);

        // Board17 목업 데이터 생성
        Board board17 = Board.builder()
                .problem(problem17)
                .user(user1)
                .memo("정규 표현식과 DFS/BFS를 통해 조합을 찾기")
                .status(BoardStatus.CORRECT)
                .description("사용 알고리즘: DFS, Regex, Set")
                .build();
        boardRepository.save(board17);

        // Solution17 목업 데이터 생성
        Solution solution17 = Solution.builder()
                .content("import java.util.*;<br>" +
                        "class Solution {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;Set&lt;Set&lt;String&gt;&gt; uniqueCases = new HashSet&lt;&gt;();<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;public int solution(String[] user_id, String[] banned_id) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;dfs(user_id, banned_id, new HashSet&lt;&gt;(), 0);<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;return uniqueCases.size();<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;void dfs(String[] user_id, String[] banned_id, Set&lt;String&gt; currentSet, int idx) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;if (idx == banned_id.length) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;uniqueCases.add(new HashSet&lt;&gt;(currentSet));<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;return;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;String pattern = banned_id[idx].replace(\"*\", \".\");<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for (String id : user_id) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;if (!currentSet.contains(id) && id.matches(pattern)) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;currentSet.add(id);<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;dfs(user_id, banned_id, currentSet, idx + 1);<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;currentSet.remove(id);<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "}")
                .language(SolutionLanguage.JAVA)
                .description("DFS를 이용해 각 불량 사용자 조합을 탐색하며, 정규 표현식을 통해 각 아이디가 조건에 맞는지 검사하여 가능한 모든 조합을 찾음.")
                .status(SolutionStatus.CORRECT)
                .memory(512)
                .time(180)
                .submitAt(LocalDateTime.now())
                .board(board17)
                .build();
        solutionRepository.save(solution17);

// Problem18 목업 데이터 생성
        Problem problem18 = Problem.builder()
                .number(60059)
                .title("자물쇠와 열쇠")
                .content("주어진 열쇠와 자물쇠의 형태를 이용해 열쇠를 회전하거나 이동시켜 자물쇠의 모든 홈에 맞는지를 확인하는 문제입니다. 자물쇠와 열쇠는 2차원 배열 형태로 주어지며, 열쇠를 회전하거나 이동하는 동작을 통해 자물쇠를 풀 수 있는지 확인해야 합니다.")
                .url("https://programmers.co.kr/learn/courses/30/lessons/60059")
                .difficulty(ProblemDifficulty.LEVEL3)
                .platform(ProblemPlatform.PROGRAMMERS)
                .build();
        problemRepository.save(problem18);

        // Board18 목업 데이터 생성
        Board board18 = Board.builder()
                .problem(problem18)
                .user(user1)
                .memo("2D 배열 회전과 이동 구현 필요")
                .status(BoardStatus.CORRECT)
                .description("사용 알고리즘: Array Manipulation, Brute Force")
                .build();
        boardRepository.save(board18);

        // Solution18 목업 데이터 생성
        Solution solution18 = Solution.builder()
                .content("class Solution {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;public boolean solution(int[][] key, int[][] lock) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;int M = key.length, N = lock.length;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;int[][] extendedLock = new int[N * 3][N * 3];<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for (int i = 0; i < N; i++)<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for (int j = 0; j < N; j++)<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;extendedLock[i + N][j + N] = lock[i][j];<br>" +
                        "<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for (int r = 0; r < 4; r++) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;key = rotate(key);<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for (int x = 0; x <= N * 2; x++) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for (int y = 0; y <= N * 2; y++) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;if (checkFit(x, y, key, extendedLock, N)) return true;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;return false;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;private int[][] rotate(int[][] key) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;int M = key.length;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;int[][] rotated = new int[M][M];<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for (int i = 0; i < M; i++)<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for (int j = 0; j < M; j++)<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;rotated[j][M - 1 - i] = key[i][j];<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;return rotated;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;private boolean checkFit(int x, int y, int[][] key, int[][] lock, int N) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for (int i = 0; i < key.length; i++)<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for (int j = 0; j < key[0].length; j++)<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;lock[x + i][y + j] += key[i][j];<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for (int i = N; i < N * 2; i++)<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for (int j = N; j < N * 2; j++)<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;if (lock[i][j] != 1) return false;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;return true;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "}")
                .language(SolutionLanguage.JAVA)
                .description("열쇠를 4방향으로 회전시키며 자물쇠에 맞춰가며 이동하여 확인함. 자물쇠와 열쇠가 맞아떨어지는 경우를 검사하고, 전체 자물쇠가 열쇠로 채워졌는지 검사.")
                .status(SolutionStatus.CORRECT)
                .memory(1024)
                .time(220)
                .submitAt(LocalDateTime.now())
                .board(board18)
                .build();
        solutionRepository.save(solution18);

// Problem19 목업 데이터 생성
        Problem problem19 = Problem.builder()
                .number(42861)
                .title("섬 연결하기")
                .content("주어진 섬들을 연결하여 최소 비용으로 모든 섬을 연결하는 문제입니다. 섬들은 특정한 비용으로 연결되며, 모든 섬이 서로 연결된 상태를 유지하면서 최소한의 비용이 드는 연결을 찾는 것이 목적입니다.")
                .url("https://programmers.co.kr/learn/courses/30/lessons/42861")
                .difficulty(ProblemDifficulty.LEVEL3)
                .platform(ProblemPlatform.PROGRAMMERS)
                .build();
        problemRepository.save(problem19);

        // Board19 목업 데이터 생성
        Board board19 = Board.builder()
                .problem(problem19)
                .user(user1)
                .memo("최소 비용 신장 트리 문제")
                .status(BoardStatus.CORRECT)
                .description("사용 알고리즘: Kruskal's Algorithm, Union-Find")
                .build();
        boardRepository.save(board19);

        // Solution19 목업 데이터 생성
        Solution solution19 = Solution.builder()
                .content("import java.util.*;<br>" +
                        "class Solution {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;public int solution(int n, int[][] costs) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Arrays.sort(costs, (a, b) -> a[2] - b[2]);<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;int[] parent = new int[n];<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for (int i = 0; i < n; i++) parent[i] = i;<br>" +
                        "<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;int totalCost = 0, edges = 0;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for (int[] cost : costs) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;int a = cost[0], b = cost[1], c = cost[2];<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;if (find(a, parent) != find(b, parent)) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;union(a, b, parent);<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;totalCost += c;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;if (++edges == n - 1) break;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;return totalCost;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;int find(int x, int[] parent) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;if (parent[x] == x) return x;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;return parent[x] = find(parent[x], parent);<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;void union(int a, int b, int[] parent) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;int rootA = find(a, parent);<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;int rootB = find(b, parent);<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;if (rootA != rootB) parent[rootA] = rootB;<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "}")
                .language(SolutionLanguage.JAVA)
                .description("Kruskal’s Algorithm을 사용하여 최소 비용 신장 트리를 구성함. 각 섬의 연결 비용을 오름차순으로 정렬하고, Union-Find 자료구조로 연결하면서 사이클이 발생하지 않도록 검사함.")
                .status(SolutionStatus.CORRECT)
                .memory(1024)
                .time(180)
                .submitAt(LocalDateTime.now())
                .board(board19)
                .build();
        solutionRepository.save(solution19);

// Problem20 목업 데이터 생성
        Problem problem20 = Problem.builder()
                .number(42888)
                .title("오픈채팅방")
                .content("사용자 아이디와 닉네임이 기록된 채팅방에서 입장, 퇴장, 닉네임 변경이 발생할 때 최종 채팅 기록을 출력하는 문제입니다. 각 사용자의 닉네임 변경 이력을 추적하여, 최종 출력 시 사용자의 마지막 닉네임을 출력합니다.")
                .url("https://programmers.co.kr/learn/courses/30/lessons/42888")
                .difficulty(ProblemDifficulty.LEVEL2)
                .platform(ProblemPlatform.PROGRAMMERS)
                .build();
        problemRepository.save(problem20);

        // Board20 목업 데이터 생성
        Board board20 = Board.builder()
                .problem(problem20)
                .user(user1)
                .memo("Map을 사용해 아이디와 닉네임을 추적")
                .status(BoardStatus.CORRECT)
                .description("사용 알고리즘: HashMap")
                .build();
        boardRepository.save(board20);

        // Solution20 목업 데이터 생성
        Solution solution20 = Solution.builder()
                .content("import java.util.*;<br>" +
                        "class Solution {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;public String[] solution(String[] record) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Map&lt;String, String&gt; map = new HashMap&lt;&gt;();<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;List&lt;String&gt; logs = new ArrayList&lt;&gt;();<br>" +
                        "<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for (String entry : record) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;String[] parts = entry.split(\" \");<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;String cmd = parts[0], uid = parts[1];<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;if (!cmd.equals(\"Leave\")) map.put(uid, parts[2]);<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for (String entry : record) {<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;String[] parts = entry.split(\" \");<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;String cmd = parts[0], uid = parts[1];<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;if (cmd.equals(\"Enter\")) logs.add(map.get(uid) + \"님이 들어왔습니다.\");<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;else if (cmd.equals(\"Leave\")) logs.add(map.get(uid) + \"님이 나갔습니다.\");<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;return logs.toArray(new String[0]);<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;}<br>" +
                        "}")
                .language(SolutionLanguage.JAVA)
                .description("각 기록을 파싱하여 해시맵에 마지막 닉네임을 저장한 후, 기록을 순회하며 닉네임과 출입 기록을 출력. 마지막 닉네임 기준으로 출입 상황을 로그로 저장함.")
                .status(SolutionStatus.CORRECT)
                .memory(512)
                .time(120)
                .submitAt(LocalDateTime.now())
                .board(board20)
                .build();
        solutionRepository.save(solution20);

        log.info("목업 데이터가 성공적으로 삽입되었습니다.");
    }
}