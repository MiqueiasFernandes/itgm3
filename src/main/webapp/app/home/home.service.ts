import { Injectable } from '@angular/core';
import {Observable} from "rxjs/Observable";

import { EventManager } from 'ng-jhipster';

import {Customize, CustomizeService} from '../entities/customize/';
import {Card, CardService} from "../entities/card";
import {Cenario} from "../entities/cenario/cenario.model";


@Injectable()
export class HomeService {

    constructor(
        private eventManager: EventManager,
        private customizeService: CustomizeService,
        private cardService: CardService,
    ) { }



    public getCards(cenario: Cenario):Observable<Card[][]> {
        if(!cenario)
            return Observable.of([]);
        return this.cardService.getCardsByCenario(cenario)
            .map((cards: Card[]) => this.mapearCards(cards));
    }


    public mapearCards(cards: Card[]):Card[][] {

        let linhas: Card[][] = [];

        cards.forEach((card: Card) => {
            if(!linhas[card.linha]) {
                linhas[card.linha] = [];
            }
            linhas[card.linha].push(card);
        });


        // linhas.forEach((cars: Card[], i) => {
        //     cars.forEach((card: Card, j) => {
        //         card.linha = i;
        //         card.coluna = j;
        //     });
        // });

        return linhas;
    }



    // public injectIPcard(cards: string[][]):Observable<{}> {
    //     return this.account.getEndereco()
    //         .map(
    //             (endereco: string) => {
    //                //endereco = endereco.replace('http:', 'https:');
    //                //  endereco = endereco.replace(':80', '');
    //                 let array: string[][] = [];
    //                 let resolver: string[] = [];
    //                 cards.forEach((linha: string[]) => {
    //                     let cols = [];
    //                     linha.forEach((coluna: string) => {
    //                         const card: string[] = this.getCard(coluna, false);
    //                         const tipo:string = card !== null ? this.getTipo(card) : null;
    //                         if(tipo) {
    //                             const cod =  this.getCodigo(card).split(',');
    //                             let url = cod[1];
    //                             if(url.indexOf('|') > 0) {
    //                                 url = url.split('|')[1];
    //                             }
    //                             card[3] = this
    //                                 .criaCodigoParaArquivo(
    //                                     cod[0],
    //                                     endereco + '|' + url,
    //                                     tipo,
    //                                     cod[4], cod[2], cod[3], cod[4], cod[5]);
    //                             cols.push(card.join(':'));
    //                             // if(tipo === 'rbokeh') {
    //                             //     resolver.push((endereco + url));
    //                             // }
    //                         }else {
    //                             cols.push(coluna);
    //                         }
    //                     });
    //                     array.push(cols);
    //                 });
    //                 return [array, resolver, endereco];
    //             });
    // }

    // public getLinhas(desktop: string): string[] {
    //     return desktop.split(';');
    // }
    //
    // public getColunas(linha: string): string[] {
    //     return linha.split(',');
    // }

    // public getCards(linhas: string[]): string[][] {
    //
    //     let array = [];
    //     linhas.forEach((linha: string) => {
    //         array.push(this.getColunas(linha));
    //     });
    //
    //     return array;
    // }

    public getAllCards(cards: Card[][]): Card[] {
        let array = [];

        cards.forEach((linha: Card[]) => {
            linha.forEach((card: Card) => {
                array.push(card);
            });
        });

        return array;
    }


    // public removeItem(linha: number, coluna: number, cards: string[][]): string[][] {
    //     const card =  cards[linha][coluna];
    //     cards[linha][coluna] = card.substring(0, card.lastIndexOf(':') + 1) + 'X';
    //
    //     const cardsV = this.getCardsValidos(cards[linha], null)[0];
    //
    //     if( !cardsV || (cardsV.length < 1)) {
    //         cards[linha] = null;
    //     }
    //     return cards;
    // }

    // public setCards(cards: string[][]) {
    //
    //     let array  = [];
    //
    //     cards.forEach((linha: string[]) => {
    //         if(linha && linha.length > 0) {
    //             array.push(linha.join(','));
    //         }
    //     });
    //
    //    // this.customizeService.customizeDesktop(array.join(';'));
    // }


    public abrirArquivo(arquivo: string, meta: any, caminho: string, previa: string) {
        const tipo: string =  this.getTipoPorArquivo(arquivo);
        const largura: number = this.getTamanhoIdealDeColuna(arquivo,tipo);

        this.customizeService.getCustomize().subscribe(
            (custom:Customize) => {
                if(custom && custom.cenario) {
                    this.getLinhaEColunaIdeal(largura, custom.cenario).subscribe(
                        (tamanho: number[]) => {
                            this.cardService.create(new Card(
                                undefined, //     id
                                arquivo, // nome
                                meta.file, // url
                                false, // https
                                JSON.stringify(meta), // meta
                                previa, // previa
                                '{\"x\":0,\"y\":0}', // disposicao
                                this.abreAberto(tipo) , // tipo - como o card esta aberto FIX invertido com modo por ser numero
                                tamanho[0],// linha
                                tamanho[1], // coluna
                                tipo,  // modo
                                caminho, // caminho
                                arquivo,  //arquivo
                                this.getExtensao(arquivo), //extensao
                                largura,  // largura
                                this.getClassePorTipo(arquivo, tipo), // classe
                                '', // codigo
                                custom.cenario // cenario
                            )).subscribe((card) => {
                                    console.log(card);
                                    this.notificar();
                                },
                                (error) => {
                                    alert('houve um erro ao abrir o card: ' + error);
                                });
                        }
                    );
                }else{
                    alert('selecione o cenario!');
                }
            }
        );
    }

    getLinhaEColunaIdeal(tamnho: number, cenario: Cenario): Observable<number[]> {
        return  this.getCards(cenario).map(
            (cards: Card[][]) => {
                let linhas: number = 0;
                let coluna: number = 0;

                cards.forEach( (cars: Card[]) =>{

                    let limite: number = 0;
                    let linha = null;
                    let colunas: number[] = []

                    cars.forEach((card: Card) => {
                        limite += card.largura;
                        linha = card.linha;
                        colunas.push(card.coluna);
                    });

                    if(linha && ((limite + tamnho) <= 12)){

                        for(let i =0; i<12; i++) {
                            if (colunas.indexOf(i) < 0) {
                                linhas = linha;
                                coluna = i;
                                return [linhas, coluna];
                            }
                        }


                    }

                    linhas++;

                });

                return [linhas, coluna];
            }
        )
    }


    public getTamanhoIdealDeColuna(url: string, tipo: string):number {
        switch (tipo) {
            case 'figura':
            case 'planilha':
            case 'rdata':
                return 2;
            case 'texto':
            case 'codigo':
                return 4;
            case 'rbokeh':
                return 6;
            default:
                return 2;
        }
    }

    public getTipoByNome(tipo: string): number{
        return ['figura', 'rbokeh', 'codigo', 'texto',  'planilha','rdata', "generic" ].indexOf(tipo);
    }

    public getNomeByTipo(tipo: number): string{
        const array = ['figura', 'rbokeh', 'codigo', 'texto',  'planilha','rdata', "generic" ];
        return array[tipo];
    }

    public getTipoPorArquivo(arquivo: string): string {
        switch (this.getExtensao(arquivo)) {
            case 'png':
            case 'jpg':
            case 'jpeg':
            case 'gif':
            case 'bmp':
            case 'tiff':
                return 'figura';
            case 'html':
                return'rbokeh';
            case 'R':
                return  'codigo';
            case 'txt':
                return 'texto';
            case 'csv':
                return 'planilha';
            case 'RData':
                return 'rdata';
            default:
                return "generic";
        }
    }

    public getExtensao(arquivo: string):string {
        const ext =  (arquivo.indexOf('.') >= 0) ? arquivo.split('.')[1] : '';
        return ext;
    }

    public getClassePorTipo(arquivo: string, tipo: string): string {
        let classe = "card jh-card";
        switch (tipo) {
            case 'figura':
                // classe += ' figura';
                break;
            case 'rbokeh':
                classe += ' meta';
                break;
            case 'texto':
                break;
            case 'codigo':
                break;
            case  'planilha':
                classe += ' meta';
                break;
            case 'rdata':
                classe += ' meta';
                break;
            default:
                classe += ' meta';
        }
        return classe;
    }

    public abreAberto(tipo: string): number {
        switch (tipo) {
            case 'figura':
            case 'rbokeh':
            case 'texto':
            case 'codigo':
                return 2;
            case  'planilha':
            case 'rdata':
            default:
                return 1;
        }
    }

    // public criaCodigoParaArquivo(nome: string, url: string, tipo: string, texto: string,
    //                              size: any, caminho: string, width: any, height: any): string {
    //     let codigo = 'X';
    //     switch (tipo) {
    //         case 'figura':
    //             return btoa(nome + ',' + url + ',' + size + ',' + caminho + ',' + width + ',' + height);
    //         case 'rbokeh':
    //         case  'planilha':
    //         case 'rdata':
    //             return btoa(nome + ',' + url+ ',' + size + ',' + caminho );
    //         case 'texto':
    //         case 'codigo':
    //             return btoa(nome + ',' + url + ',' + size + ',' + caminho + ',' + texto);
    //     }
    //     return codigo;
    // }

    // public insereCardOrdenado(coluna: number, classe: string, tipo: string, codigo: string) {
    //
    //     this.getDesktop().subscribe(
    //         (cards: string[][]) => {
    //             let array = [];
    //             let add = false;
    //             let added;
    //             cards.forEach((linha: string[]) => {
    //                 if(linha && linha.length > 0) {
    //                     if (!add && ((this.tamanhoColuna(linha) + coluna) <= 12)) {
    //                         const resp :[string[], boolean] = this.getCardsValidos(linha,
    //                             (added = (coluna + ':' + classe + ':' + tipo + ':' + codigo)));
    //                         linha = resp[0];
    //                         add = resp[1];
    //                     }
    //                     array.push(linha);
    //                 }
    //             });
    //             if (!add) {
    //                 cards.push([added = (coluna + ':' + classe + ':' + tipo + ':' + codigo)]);
    //             }else{
    //                 cards = array;
    //             }
    //             this.setCards(cards);
    //         }
    //     );
    //
    // }

    /// tamanho:classe:tipo:codigo

    // public isCardValido(card: string, aceitaX: boolean): boolean {
    //     let parts = [];
    //     return (card &&
    //         (card.length > 0) &&
    //         (parts = card.split(':')).length === 4 &&
    //         (parts[0] && (parts[0].length > 0)) && (parseInt(parts[0]) <= 12)
    //         && parts[1] && parts[1].length > 2
    //         && parts[2] && parts[2].length > 2
    //         && (parts[3]) && (parts[3].length > 1 || (aceitaX && parts[3] === 'X'))
    //     );
    // }

    // public getCard(card: string, aceitaX: boolean):string[] {
    //     return this.isCardValido(card, aceitaX) ? card.split(':') : null;
    // }

    // public getTamanhoColuna(card: string[]):number {
    //     return parseInt(card[0]);
    // }

    // public getClasse(card: string[]):string {
    //     return card[1];
    // }

    // public getTipo(card: string[]):string {
    //     return card[2];
    // }

    // public getCodigo(card: string[]):string {
    //     return card[3] !== 'X' ? atob(card[3]) : 'X';
    // }

    // public getCardsValidos(cards: string[], substituto: string):[string[], boolean] {
    //     let array = [];
    //
    //     cards.forEach((card: string) => {
    //         if(this.isCardValido(card, false)) {
    //             array.push(card);
    //         } else if(substituto) {
    //             array.push(substituto);
    //             substituto = null;
    //         }
    //     });
    //
    //     if(substituto && ((this.tamanhoColuna(cards) + parseInt(substituto.split(':')[0])) <= 12 )) {
    //         array.push(substituto);
    //         substituto = null;
    //     }
    //
    //     return [array, substituto === null];
    // }

    // public tamanhoColuna(cards: string[]):number {
    //     const crds = this.getCardsValidos(cards, null)[0];
    //     let tam: number = 0;
    //     if(crds && crds.length > 0) {
    //         crds.forEach((card: string) => {
    //             tam += parseInt(card.split(':')[0]);
    //         });
    //     }
    //     return tam;
    // }

    public isText(file: string):boolean {
        return ['texto', 'codigo'].indexOf(this.getTipoPorArquivo(file)) >= 0;
    }

    public getNativeWnidow(){
        return window;
    }



    public reduzirCard(card: Card): Observable<Card> {
        card.tipo = 1;
        return this.cardService.update(card)
            .map((res) => {
            this.notificar();
            return res;
        });
    }

    public ampliarCard(card: Card): Observable<Card>  {
        card.tipo = 2;
        return this.cardService.update(card)
            .map((res) => {
            this.notificar();
            return res;
        });
    }

    public removeCard(id: number):Observable<boolean> {
        return this.cardService.delete(id)
            .map(() => {
               // this.notificar();
                return true;
            });
    }

    private notificar() {
        this.eventManager
            .broadcast({ name: 'cardListModification', content: 'OK'});
    }

}
