//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Supermercado {
    private static int quantidade = 0;
    private static Object[][] estoque = new Object[5][9];

    public Supermercado() {
    }

    public static void main(String[] args) {
        menu();
    }

    public static void menu() {
        Scanner sc = new Scanner(System.in);

        String opcao;
        do {
            while(true) {
                System.out.println("Por favor, selecione o item desejado");
                System.out.println("0 - Saida");
                System.out.println("1 - Cadastrar/Comprar produtos");
                System.out.println("2 - Imprimir estoque");
                System.out.println("3 - Listar os produto pelo Tipo");
                opcao = sc.next();
                if (!opcao.equals("1") && !opcao.equals("2") && !opcao.equals("3")) {
                    break;
                }

                sistema(opcao);
            }
        } while(!opcao.equals("0"));

        System.out.println("Saindo do sistema! Tchau, volte sempre!");
    }

    public static void sistema(String opcao) {
        byte var3 = -1;
        switch(opcao.hashCode()) {
            case 49:
                if (opcao.equals("1")) {
                    var3 = 0;
                }
                break;
            case 50:
                if (opcao.equals("2")) {
                    var3 = 1;
                }
                break;
            case 51:
                if (opcao.equals("3")) {
                    var3 = 2;
                }
        }

        switch(var3) {
            case 0:
                Object[] infos = inserirInfos();
                int status = verificarCadastro(infos);
                if (status == -1) {
                    cadastrar(infos);
                } else {
                    atualizar(infos, status);
                }

                dadosDaCompra(infos);
                break;
            case 1:
                imprimirEstoque();
            case 2:
                break;
            default:
                System.out.println("Saindo do sistema! Tchau, volte sempre!");
        }

    }

    public static Object[] inserirInfos() {
        Scanner sc = new Scanner(System.in);
        Object[] infos = new Object[6];
        System.out.println("Tipo: ");
        infos[0] = sc.nextLine();
        System.out.println("Marca: ");
        infos[1] = sc.nextLine();
        System.out.println("Identificador: ");
        infos[2] = sc.nextLine();
        System.out.println("Nome: ");
        infos[3] = sc.nextLine();
        System.out.println("Preco Custo: ");
        infos[4] = sc.nextDouble();
        System.out.println("Quantidade: ");
        infos[5] = sc.nextInt();
        return infos;
    }

    public static int verificarCadastro(Object[] infos) {
        if (quantidade == 0) {
            return -1;
        } else {
            for(int i = 0; i < quantidade; ++i) {
                if (estoque[i][1].equals(infos[1]) && estoque[i][2].equals(infos[2])) {
                    return i;
                }
            }

            return -1;
        }
    }

    public static void cadastrar(Object[] infos) {
        if (quantidade == estoque.length) {
            redimensionarEficiente();
        }

        for(int i = 0; i < infos.length; ++i) {
            estoque[quantidade][i] = infos[i];
        }

        estoque[quantidade][8] = 0;
        ++quantidade;
    }

    public static void redimensionarEficiente() {
        Object[][] newArray = new Object[estoque.length * 2][estoque[0].length];

        for(int i = 0; i < estoque.length; ++i) {
            for(int i1 = 0; i1 < estoque[i].length; ++i1) {
                newArray[i][i1] = estoque[i][i1];
            }
        }

        estoque = newArray;
    }

    public static void atualizar(Object[] infos, int posicao) {
        for(int i = 0; i < infos.length; ++i) {
            estoque[posicao][i] = infos[i];
        }

    }

    public static void dadosDaCompra(Object[] infos) {
        LocalDateTime datetime1 = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String formatDateTime = datetime1.format(format);
        estoque[quantidade - 1][6] = formatDateTime;
        if (estoque[quantidade - 1][0] instanceof String) {
            String nomeTipo = String.valueOf(estoque[quantidade - 1][0]);
            estoque[quantidade - 1][7] = (Double)estoque[quantidade - 1][4] * (double)Tipo.valueOf(nomeTipo).getMarkup();
        }

        if (estoque[quantidade - 1][8] instanceof Integer && infos[5] instanceof Integer) {
            int quantidadeEstoque = (Integer)estoque[quantidade - 1][8];
            int quantidadeCompra = (Integer)infos[5];
            estoque[quantidade - 1][8] = quantidadeEstoque + quantidadeCompra;
        }

    }

    public static void imprimirEstoque() {
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-10s %-20s %-15s %-20s %-15s %-12s %-20s %-15s %-10s", "Tipo", "Marca", "Identificador", "Nome", "Preco Custo", "Quantidade", "Data Compra", "Preco de venda", "Estoque");
        System.out.println();
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------");

        for(int i = 0; i < quantidade; ++i) {
            System.out.format("%-10s %-20s %-15s %-20s %-15.2f %-12d %-20s %-15.2f %-10d", estoque[i][0], estoque[i][1], estoque[i][2], estoque[i][3], (Double)estoque[i][4], (Integer)estoque[i][5], estoque[i][6], (Double)estoque[i][7], (Integer)estoque[i][8]);
            System.out.println();
        }

        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------");
    }
}
