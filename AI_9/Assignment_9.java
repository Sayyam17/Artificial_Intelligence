import java.util.*;

public class Assignment_9 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Map<String, String> responses = new LinkedHashMap<>();

        // Medical responses
        responses.put("fever", "It seems like you have a fever. Stay hydrated and rest. If it persists for more than 3 days, visit a doctor.");
        responses.put("cough", "A cough could be due to a cold or allergy. Drink warm water and consider steam inhalation.");
        responses.put("headache", "Headaches can be caused by stress or dehydration. Drink water and take rest.");
        responses.put("stomach pain", "You might have indigestion. Avoid oily food and eat light meals.");
        responses.put("cold", "A common cold can be managed with rest and fluids. If it lasts more than a week, see a physician.");
        responses.put("chest pain", "Chest pain can be serious. Please seek immediate medical attention!");
        responses.put("tired", "You might be fatigued. Make sure you’re getting enough sleep and a balanced diet.");
        responses.put("vomit", "Vomiting may be caused by infection or food poisoning. Stay hydrated and rest.");
        responses.put("body pain", "It may be due to fatigue or viral infection. Take rest and drink enough water.");

        // General conversation responses
        responses.put("hi", "Hello! How can I assist you today?");
        responses.put("hello", "Hi there! Tell me your symptoms or how you feel.");
        responses.put("hey", "Hey! I’m here to help you with your health concerns.");
        responses.put("who are you", "I’m your virtual medical assistant chatbot.");
        responses.put("thank you", "You're welcome! I'm glad I could help. 😊");
        responses.put("thanks", "No problem! Take care of your health!");
        responses.put("ok", "Alright! Let me know if you have more symptoms or questions.");
        responses.put("okay", "Sure! I'm here whenever you need help.");
        responses.put("bye", "Goodbye! Wishing you good health. 👋");
        responses.put("goodbye", "Take care! Have a healthy day ahead!");
        responses.put("how are you", "I'm doing great! Ready to help you with your health questions.");
        responses.put("what can you do", "I can help you understand common symptoms and give basic health advice.");

        System.out.println("👩‍⚕️ Welcome to the Medical Chatbot!");
        System.out.println("You can talk to me about symptoms or just chat normally.");
        System.out.println("Type 'exit' to end the chat.\n");

        while (true) {
            System.out.print("You: ");
            String input = sc.nextLine().toLowerCase().trim();

            if (input.equals("exit")) {
                System.out.println("Chatbot: Take care! Wishing you good health. 👋");
                break;
            }

            boolean found = false;
            for (String key : responses.keySet()) {
                if (input.contains(key)) {
                    System.out.println("Chatbot: " + responses.get(key));
                    found = true;
                    break;
                }
            }

            if (!found) {
                System.out.println("Chatbot: I'm not sure about that. Could you tell me your symptom or ask me a health question?");
            }
        }
    }
}
