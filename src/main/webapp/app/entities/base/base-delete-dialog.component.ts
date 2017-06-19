import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, JhiLanguageService } from 'ng-jhipster';

import { Base } from './base.model';
import { BasePopupService } from './base-popup.service';
import { BaseService } from './base.service';

@Component({
    selector: 'jhi-base-delete-dialog',
    templateUrl: './base-delete-dialog.component.html'
})
export class BaseDeleteDialogComponent {

    base: Base;

    constructor(
        private jhiLanguageService: JhiLanguageService,
        private baseService: BaseService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
        this.jhiLanguageService.setLocations(['base']);
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.baseService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'baseListModification',
                content: 'Deleted an base'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-base-delete-popup',
    template: ''
})
export class BaseDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private basePopupService: BasePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.basePopupService
                .open(BaseDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
