import java.util.List;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramBotAdapter;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;

public class Main {

	public static void main(String[] args) {

		// Criação do objeto bot com as informações de acesso
		// token do ilachatbot: 286692203:AAFxPRqQUk8lcy1z7N4oZtIQImh_QvWtHTQ
		TelegramBot bot = TelegramBotAdapter.build("286692203:AAFxPRqQUk8lcy1z7N4oZtIQImh_QvWtHTQ");

		// objeto responsável por receber as mensagens
		GetUpdatesResponse updatesResponse;

		// objeto responsável por gerenciar o envio de respostas
		SendResponse sendResponse;

		// objeto responsável por gerenciar o envio de ações do chat
		// Chat Actions: que são os feedback ao usuário que o bot está
		// trabalhando
		// possuem as possíveis atualizações de status, como “Escrevendo…”,
		// “Enviando Arquivo…”, etc
		BaseResponse baseResponse;

		// controle de off-set, isto é, a partir deste ID será lido as mensagens
		// pendentes na fila
		int m = 0;

		// loop infinito pode ser alterado por algum timer de intervalo curto
		while (true) {
			// executa comando no Telegram para obter as mensagens pendentes a
			// partir de um off-set (limite inicial)
			updatesResponse = bot.execute(new GetUpdates().limit(100).offset(m));

			// lista de mensagens
			List<Update> updates = updatesResponse.updates();

			// análise de cada ação de cada mensagem
			for (Update update : updates) {
				// atualização do off-set
				m = update.updateId() + 1;
				// recebendo a mensagem
				String msg = update.message().text();
				System.out.println("Recebendo mensagem:" + msg);

				// envio de "Escrevendo" antes de enviar a resposta
				baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
				// verificação de ação de chat foi enviada com sucesso
				System.out.println("Resposta de Chat Action Enviada? " + baseResponse.isOk());

				try{
					// processar a mensagem recebida
					switch (msg) {
						case "oi":
							// envio da mensagem de resposta
							sendResponse = bot
									.execute(new SendMessage(update.message().chat().id(), "Oi, em que posso ajudar?"));
							// verificação de mensagem enviada com sucesso
							System.out.println("Resposta Oi Enviada? " + sendResponse.isOk());
							break;
						case "ILA":
							// envio da mensagem de resposta
							sendResponse = bot.execute(new SendMessage(update.message().chat().id(),
									"ILA \n "
									+ "Instituto de Logística da Aeronáutica"));
							// verificação de mensagem enviada com sucesso
							System.out.println("ILA: " + sendResponse.isOk());
							break;
						case "obrigado":
							// envio da mensagem de resposta
							sendResponse = bot.execute(new SendMessage(update.message().chat().id(),
									"Por nada, se precisar pode contar comigo!!! \n Tchal..."));
							// verificação de mensagem enviada com sucesso
							System.out.println("Resposta Tchal Enviada? " + sendResponse.isOk());
							break;
						case "/start":
							// envio da mensagem de resposta
							sendResponse = bot.execute(new SendMessage(update.message().chat().id(),
									"Oi, eu sou o ChatRobô do ILA :cop: \n"+
							"Estou aqui para lhe ajudar em pequenas dúvidas referentes à capacitação no Instituto (Cursos, Processo de indicação, Emissão de Certificado, Plano de Avaliação, etc). \n"+ 
							"Teste meus comandos abaixo, ou envie /help para mais informações. \n"+
							"Desde já agradeço sua atenção :+1: \n"+
							"Se após nossa conversa ainda restar dúvidas, ligue: (11)2465-2007!"));
							// verificação de mensagem enviada com sucesso
							System.out.println("Start: " + sendResponse.isOk());
							break;
						default:
							// envio da mensagem de resposta
							sendResponse = bot.execute(new SendMessage(update.message().chat().id(),
									"Não entendi \n Tente ser mais claro em sua dúvida!"));
							// verificação de mensagem enviada com sucesso
							System.out.println("Mensagem N Entendi Enviada? " + sendResponse.isOk());
							break;
					}
				}catch (NullPointerException e) {
					// a mensagem enviada não é baseada unicamente em texto
					// envio da mensagem de resposta
					sendResponse = bot.execute(new SendMessage(update.message().chat().id(),
							"Dexia eu analisar..... \n"
							+ "Não entendi bem o que você quis dizer \n "
							+ "Tente ser mais claro em sua dúvida e use apenas textos!"));
					// verificação de mensagem enviada com sucesso
					System.out.println("Mensagem N Entendi Enviada? " + sendResponse.isOk());
				}
			}
		}

	}
}
