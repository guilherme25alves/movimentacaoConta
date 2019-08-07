# Projeto Movimentações de conta do cliente

### Explicação e linha de pensamento utilizada.

O projeto em questão se trata de acesso a informações provenientes da API https://my-json-server.typicode.com/cairano/backend-test/ e também de um arquivo de log criado com base nas informações que foram passadas nesse desafio.

Foi criado classes Bean para representar as duas formas de movimentar a conta, seja recebendo ou efetuando algum tipo de pagamento. Tanto a classe de pagamentos como a de recebimentos ficaram com as propriedades data, descricao, moeda, valor, exceto categoria que ficou apenas na classe de pagamentos.

As 2 classes beans possuem um método que faz a interação direta com o API por meio do método GET para recuperar as informações provenientes da mesma. O objeto é recuperado em formato de String, para facilitar o tratamento dos dados, sendo convertidos posteriormente para o tipo de dado que cada propriedade se adequa de melhor modo. Um método com o tipo POST também foi criado, mas apenas para efeitos de teste.

No pacote principal do projeto, temos 2 classes, a App e a LogMovimentacao. A primeira possui a lógica principal do projeto, trazendo um pequeno relatório com o que foi proposto para o desafio. A segunda se trata da classe que vai ler e tratar os dados do arquivo de log com as transações mencionadas.

Para auxílio na execução do projeto, foi usado a biblioteca Gson do Google. Com ela foi possível ler o arquivo com a classe de BufferedReader e realizar um loop para ler cada linha, convertendo elas em um JsonObject, sendo  que cada uma representava uma movimentação. Uma condição é validada para descobrir qual o tipo de movimentação, no caso, os valores negativos, representavam pagamentos, logo era adicionado a um JsonArray do tipo PagamentoBean e criado um JsonObject para cada, caso contrário, o mesmo procedimento era feito, mas para RecebimentoBean.

Ao longo do processo dessa classe de log, algumas funçoes de String são utilizadas para limpeza e similaridade dos dados, como retirada de espaços desnecessárias, a divisão de cada parte da String para saber onde se iniciava e terminava cada dado, o tratamento do caractere de valor para se enquadrar no tipo Float utilizado mais tarde para conversão dos dados para cálculos e a tradução dos meses para o português com o replace de Strings.

Na classe principal App, eu desenvolvi um método main a parte para ser executado, que vai trazer um menu no console, com algumas opções para ver cada item proposto separadamente e também tudo junto, como um relatório completo, com opções do 1 ao 7, digitando o valor 9, se encerra o programa, com qualquer outro valor, o programa retorna uma mensagem informando que é inválido e pedido que o usuário digite novamente um valor numérico válido.

Todas as variáveis para o relatório são declaradas na classe App. Primeiramente são declaradas listas de cada tipo de movimentação, os valores que vem da API são recuperados e um código é implementado para que haja uma união entre os dados do arquivo de log e também da API, sendo que os dois passam pelas mesmas alterações e limpeza com ajuda dos métodos de Strings para manter a regularidade e padrão entre os dados para uma análise mais acertiva.
Para gerar os dados e relatórios um laço de repetição acontece passando pela lista de recebimentos e de pagamentos e conforme fosse se enquadrando em cada item, o atributo criado para gerar o relatório era incrementado.
Ao fim, todos os dados são dispostos via console terminal, executando o método main da classe App.

O texto acima, explica um pouco de como desenvolvi essa lógica para cumprir o desafio, foi baseado e feito conforme interpretei a situação, quaisquer dúvidas ou questionamentos, favor entrar em contato. Gostaria por meio dessa mensagem, agradecer a oportunidade e também a chance de praticar e ampliar meus conhecimentos com esse novo desafio.
Contato : guilhermealves2596@gmail.com
