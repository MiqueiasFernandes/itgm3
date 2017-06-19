import {Component, OnInit} from "@angular/core";

import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {Response} from "@angular/http";
import {AlertService} from "ng-jhipster";
import {Customize, CustomizeService} from '../../customize';
import {Base, BaseService} from "../index";

@Component({
    selector: 'jhi-fab-add-base',
    templateUrl: './fab-add-base.component.html',
    styles: [],
})
export class FabAddBaseComponent implements OnInit {

    file: File;
    tipo: number;
    onLoad = false;
    extra = '';
    visivel = false;
    variaveis: string[] = [];

    constructor(
        public activeModal: NgbActiveModal,
        private customizeService: CustomizeService,
        private alertService: AlertService,
        private baseService: BaseService
    ) {}

    ngOnInit() {
    }

    adicionarBase() {

        if (!this.file) {
            this.onError({message: 'selecione o arquivo!'});
            return;
        }

        this.customizeService.getCustomize()
            .subscribe((customize: Customize) => {
                if(customize && customize.projeto) {
                    let base: Base =
                        new Base(null, this.file.name, null, customize.projeto);
                    if (!base.projeto || base.projeto === null || base.projeto === undefined) {
                        this.onError({message: 'ative um projeto!'});
                        return;
                    }

                    this.baseService.create(base)
                        .subscribe(
                            (res: Base) => this.onSaveSuccess(res),
                            (res: Response) => this.onError(res.json()));
                }
            });
    }

    setFile($event) {
        this.file = $event.target.files[0];
        if (this.file.name.endsWith('.csv')) {
            this.tipo = 1;
            this.extra = ';';
        } else if (this.file.name.endsWith('.RData')) {
            this.tipo = 2;
            this.extra = '';
        } else {
            this.tipo = 0;
            this.onError({message: 'arquivo invalido!'});
            this.file = null;
            return;
        }
    }

    onSaveSuccess(base: Base) {
        this.onLoad = true;
        this.baseService.sendBase(base, this.file, this.extra).subscribe(
            (res) => {
                this.alertService.success(res.toString());
                this.close();
            },
            (err) => {
                this.onError(err);
            }
        )
    }

    getVariaveis() {
        this.customizeService.getCustomize()
            .subscribe((customize: Customize) => {
                if (customize && customize.projeto) {
                    this.baseService.sendBaseToTemp(
                        customize.projeto.user.login,
                        customize.projeto.nome,
                        this.file,
                        '999')
                        .map((res: Response) => res.json())
                        .subscribe(
                            (data) => {
                                if (data.sucesso) {
                                    this.baseService.getVarsOfBase(
                                        customize.projeto.user.login,
                                        customize.projeto.nome,
                                        '999'
                                    ).subscribe(
                                        (vars: string[]) => {
                                            this.variaveis = vars;
                                            this.visivel = true;
                                        }
                                    )
                                }
                            }
                        );
                }
            });
    }

    private onError(error) {
        this.alertService.error(error.message);
    }

    private close() {
        this.activeModal.dismiss('closed');
    }

    toogleDropdown() {
        this.visivel = (this.onLoad ? false : (!this.visivel));
    }

    setVar(variavel: string){
        this.visivel = false;
        this.extra = variavel;
    }

}
