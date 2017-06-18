import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService, JhiLanguageService, DataUtils } from 'ng-jhipster';

import { Customize } from './customize.model';
import { CustomizePopupService } from './customize-popup.service';
import { CustomizeService } from './customize.service';
import { User, UserService } from '../../shared';
import { Projeto, ProjetoService } from '../projeto';
import { Cenario, CenarioService } from '../cenario';

@Component({
    selector: 'jhi-customize-dialog',
    templateUrl: './customize-dialog.component.html'
})
export class CustomizeDialogComponent implements OnInit {

    customize: Customize;
    authorities: any[];
    isSaving: boolean;

    users: User[];

    projetos: Projeto[];

    cenarios: Cenario[];
    constructor(
        public activeModal: NgbActiveModal,
        private jhiLanguageService: JhiLanguageService,
        private dataUtils: DataUtils,
        private alertService: AlertService,
        private customizeService: CustomizeService,
        private userService: UserService,
        private projetoService: ProjetoService,
        private cenarioService: CenarioService,
        private eventManager: EventManager
    ) {
        this.jhiLanguageService.setLocations(['customize']);
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.userService.query().subscribe(
            (res: Response) => { this.users = res.json(); }, (res: Response) => this.onError(res.json()));
        this.projetoService.query({filter: 'customize-is-null'}).subscribe((res: Response) => {
            if (!this.customize.projeto || !this.customize.projeto.id) {
                this.projetos = res.json();
            } else {
                this.projetoService.find(this.customize.projeto.id).subscribe((subRes: Projeto) => {
                    this.projetos = [subRes].concat(res.json());
                }, (subRes: Response) => this.onError(subRes.json()));
            }
        }, (res: Response) => this.onError(res.json()));
        this.cenarioService.query({filter: 'customize-is-null'}).subscribe((res: Response) => {
            if (!this.customize.cenario || !this.customize.cenario.id) {
                this.cenarios = res.json();
            } else {
                this.cenarioService.find(this.customize.cenario.id).subscribe((subRes: Cenario) => {
                    this.cenarios = [subRes].concat(res.json());
                }, (subRes: Response) => this.onError(subRes.json()));
            }
        }, (res: Response) => this.onError(res.json()));
    }
    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, customize, field, isImage) {
        if (event.target.files && event.target.files[0]) {
            const file = event.target.files[0];
            if (isImage && !/^image\//.test(file.type)) {
                return;
            }
            this.dataUtils.toBase64(file, (base64Data) => {
                customize[field] = base64Data;
                customize[`${field}ContentType`] = file.type;
            });
        }
    }
    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.customize.id !== undefined) {
            this.customizeService.update(this.customize)
                .subscribe((res: Customize) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
        } else {
            this.customizeService.create(this.customize)
                .subscribe((res: Customize) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
        }
    }

    private onSaveSuccess(result: Customize) {
        this.eventManager.broadcast({ name: 'customizeListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    trackUserById(index: number, item: User) {
        return item.id;
    }

    trackProjetoById(index: number, item: Projeto) {
        return item.id;
    }

    trackCenarioById(index: number, item: Cenario) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-customize-popup',
    template: ''
})
export class CustomizePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private customizePopupService: CustomizePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.customizePopupService
                    .open(CustomizeDialogComponent, params['id']);
            } else {
                this.modalRef = this.customizePopupService
                    .open(CustomizeDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
