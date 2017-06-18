import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ItgmSharedModule } from '../../shared';
import { ItgmAdminModule } from '../../admin/admin.module';
import {
    CompartilharService,
    CompartilharPopupService,
    CompartilharComponent,
    CompartilharDetailComponent,
    CompartilharDialogComponent,
    CompartilharPopupComponent,
    CompartilharDeletePopupComponent,
    CompartilharDeleteDialogComponent,
    compartilharRoute,
    compartilharPopupRoute,
    CompartilharResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...compartilharRoute,
    ...compartilharPopupRoute,
];

@NgModule({
    imports: [
        ItgmSharedModule,
        ItgmAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        CompartilharComponent,
        CompartilharDetailComponent,
        CompartilharDialogComponent,
        CompartilharDeleteDialogComponent,
        CompartilharPopupComponent,
        CompartilharDeletePopupComponent,
    ],
    entryComponents: [
        CompartilharComponent,
        CompartilharDialogComponent,
        CompartilharPopupComponent,
        CompartilharDeleteDialogComponent,
        CompartilharDeletePopupComponent,
    ],
    providers: [
        CompartilharService,
        CompartilharPopupService,
        CompartilharResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ItgmCompartilharModule {}
