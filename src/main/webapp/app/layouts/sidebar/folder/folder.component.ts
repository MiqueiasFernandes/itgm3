import { Component, Input } from '@angular/core';

import {Customize, CustomizeService,CenarioService} from '../../../entities';

import {HomeService} from '../../../home/home.service';

@Component({
    selector: 'jhi-folder',
    templateUrl: './folder.component.html',
    styleUrls: [
        './folder.scss'
    ]
})
export class FolderComponent {

    @Input() pai: string;
    @Input() arquivos: string[];

    pastaAberta = [];
    loading = [];

    conhecidos = [
        'png', 'jpg', 'jpeg', 'gif',
        'doc', 'docx', 'txt',
        'csv', 'xls', 'xlsx',
        'R', 'RData', 'rbokeh', 'html'
    ];

    constructor(
        private cenarioService: CenarioService,
        private  customizeService: CustomizeService,
        private homeService: HomeService,
    ) {
        this.loading = [];
    }


    getPastas():string[] {

        const pastas = [];

        this.arquivos.forEach((arq: string) => {
            if ((arq.indexOf('/') >= 0)) {
                const pasta = arq.split('/')[0] + '/';
                if (pastas.indexOf(pasta) < 0) {
                    pastas.push(pasta);
                }
            }
        });

        return pastas;
    }

    getArquivos():string[] {

        const arquivos = [];

        this.arquivos.forEach((arq: string) => {
            if (arq.indexOf('/') <= 0 && arq.length > 0) {
                arquivos.push(arq);
            }
        });

        return arquivos;
    }

    getProximo():string[] {

        const ret = [];

        this.arquivos.forEach((arq: string) => {
            if ((arq.indexOf('/') >= 0)) {
                const pasta = arq.split('/')[0] + '/';
                if (pasta != arq) {
                    if (!ret[pasta])
                        ret[pasta] = arq.substring(pasta.length);
                    else
                        ret[pasta] += ',' + arq.substring(pasta.length);
                }
            }
        });

        return ret;
    }

    alterar(pasta: string) {

        if(this.pastaAberta[pasta]){
            this.pastaAberta[pasta] = false;
        }else{
            this.pastaAberta[pasta] = true;
        }

    }


    getImg(file: string): string{
        if(file.indexOf('.') > 0 ) {
            return '../../../../content/images/ext/' + file.substring(file.indexOf('.') -1) + 'png';
        }
        return null;
    }

    getExt(file: string, ext: string[]):boolean {

        let certo = false;

        ext.forEach((extensao: string) => {
            if(file.endsWith(extensao)){
                certo = true;
            }
        })

        return certo;
    }

    show(file){
        ////publicar arquivo
        this.loading[file] = true;
        this.customizeService.getCustomize()
            .subscribe((custom: Customize) => {
                    if(custom.cenario) {
                        const caminho = this.pai.substring(custom.cenario.caminho.length);
                        this.cenarioService.publicarArquivo(
                            custom.cenario,
                            caminho,
                            file,
                            this.homeService.isText(file),
                            this.homeService.getTipoPorArquivo(file) === 'figura'
                        )
                            .subscribe((nfile: any) => {
                                    if (this.homeService.isText(file)) {
                                        this.cenarioService.publicarArquivo(
                                            custom.cenario,
                                            this.pai.substring(custom.cenario.caminho.length),
                                            file,
                                            false,
                                            false
                                        )
                                            .subscribe((url: any) => {
                                                    this.homeService.abrirArquivo(
                                                        file,
                                                        url,
                                                        // url.file,
                                                        // nfile.file,
                                                        // url.size,
                                                        caminho,
                                                        nfile.file
                                                        // url.width,
                                                        // url.height
                                                    );
                                                },
                                                (error) => {
                                                    alert('152) houve um erro: ' + error.json());
                                                    this.pararLoading(file);
                                                });
                                    } else {
                                        this.homeService.abrirArquivo(
                                            file,
                                            nfile,
                                            // nfile.file,
                                            // null,
                                            // nfile.size,
                                            caminho,
                                            // nfile.width,
                                            // nfile.height,
                                            'formato imprevisivel'
                                        );
                                    }
                                    this.pararLoading(file);
                                },
                                (error) => {
                                    alert('169) houve um erro: ' + error.json());
                                    this.pararLoading(file);
                                }
                            );
                    } else {
                        alert('173) selecione um cenario!');
                    }
                },
                (error) => {
                    alert('177) houve um erro: ' + error.json());
                    this.pararLoading(file);
                }
            );

    }

    public pararLoading(file: string){
        this.loading[file] = false;
    }

}
