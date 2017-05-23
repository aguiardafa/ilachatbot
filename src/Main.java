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

		// Cria��o do objeto bot com as informa��es de acesso
		// token do ilachatbot: 286692203:AAFxPRqQUk8lcy1z7N4oZtIQImh_QvWtHTQ
		TelegramBot bot = TelegramBotAdapter.build("286692203:AAFxPRqQUk8lcy1z7N4oZtIQImh_QvWtHTQ");

		// objeto respons�vel por receber as mensagens
		GetUpdatesResponse updatesResponse;

		// objeto respons�vel por gerenciar o envio de respostas
		SendResponse sendResponse;

		// objeto respons�vel por gerenciar o envio de a��es do chat
		// Chat Actions: que s�o os feedback ao usu�rio que o bot est�
		// trabalhando
		// possuem as poss�veis atualiza��es de status, como �Escrevendo��,
		// �Enviando Arquivo��, etc
		BaseResponse baseResponse;

		// controle de off-set, isto �, a partir deste ID ser� lido as mensagens
		// pendentes na fila
		int m = 0;

		// loop infinito pode ser alterado por algum timer de intervalo curto
		while (true) {
			// executa comando no Telegram para obter as mensagens pendentes a
			// partir de um off-set (limite inicial)
			updatesResponse = bot.execute(new GetUpdates().limit(100).offset(m));

			// lista de mensagens
			List<Update> updates = updatesResponse.updates();

			// an�lise de cada a��o de cada mensagem
			for (Update update : updates) {
				// atualiza��o do off-set
				m = update.updateId() + 1;
				// recebendo a mensagem
				String msg = update.message().text();
				System.out.println("Recebendo mensagem:" + msg);

				// envio de "Escrevendo" antes de enviar a resposta
				baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
				// verifica��o de a��o de chat foi enviada com sucesso
				System.out.println("Resposta de Chat Action Enviada? " + baseResponse.isOk());

				try{
					// processar a mensagem recebida
					switch (msg) {
						case "oi":
							// envio da mensagem de resposta
							sendResponse = bot
									.execute(new SendMessage(update.message().chat().id(), "Oi, em que posso ajudar?"));
							// verifica��o de mensagem enviada com sucesso
							System.out.println("Resposta Oi Enviada? " + sendResponse.isOk());
							break;
						case "ILA":
							// envio da mensagem de resposta
							sendResponse = bot.execute(new SendMessage(update.message().chat().id(),
									"ILA \n "
									+ "Instituto de Log�stica da Aeron�utica"));
							// verifica��o de mensagem enviada com sucesso
							System.out.println("ILA: " + sendResponse.isOk());
							break;
						case "obrigado":
							// envio da mensagem de resposta
							sendResponse = bot.execute(new SendMessage(update.message().chat().id(),
									"Por nada, se precisar pode contar comigo!!! \n Tchal..."));
							// verifica��o de mensagem enviada com sucesso
							System.out.println("Resposta Tchal Enviada? " + sendResponse.isOk());
							break;
						case "/start":
							// envio da mensagem de resposta
							sendResponse = bot.execute(new SendMessage(update.message().chat().id(),
									"Oi, eu sou o ChatRob� do ILA :cop: \n"+
							"Estou aqui para lhe ajudar em pequenas d�vidas referentes � capacita��o no Instituto (Cursos, Processo de indica��o, Emiss�o de Certificado, Plano de Avalia��o, etc). \n"+ 
							"Teste meus comandos abaixo, ou envie /help para mais informa��es. \n"+
							"Desde j� agrade�o sua aten��o :+1: \n"+
							"Se ap�s nossa conversa ainda restar d�vidas, ligue: (11)2465-2007!"));
							// verifica��o de mensagem enviada com sucesso
							System.out.println("Start: " + sendResponse.isOk());
							break;
						default:
							// envio da mensagem de resposta
							sendResponse = bot.execute(new SendMessage(update.message().chat().id(),
									"N�o entendi \n Tente ser mais claro em sua d�vida!"));
							// verifica��o de mensagem enviada com sucesso
							System.out.println("Mensagem N Entendi Enviada? " + sendResponse.isOk());
							break;
					}
				}catch (NullPointerException e) {
					// a mensagem enviada n�o � baseada unicamente em texto
					// envio da mensagem de resposta
					sendResponse = bot.execute(new SendMessage(update.message().chat().id(),
							"Dexia eu analisar..... \n"
							+ "N�o entendi bem o que voc� quis dizer \n "
							+ "Tente ser mais claro em sua d�vida e use apenas textos!"));
					// verifica��o de mensagem enviada com sucesso
					System.out.println("Mensagem N Entendi Enviada? " + sendResponse.isOk());
				}
			}
		}

	}
}
